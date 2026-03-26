package com.ruoyi.Xidian.service;

import com.rabbitmq.client.Channel;
import com.ruoyi.Xidian.domain.Task;
import com.ruoyi.Xidian.domain.TaskDataGroup;
import com.ruoyi.Xidian.domain.TaskDataMetric;
import com.ruoyi.Xidian.domain.enums.TaskStatusEnum;
import com.ruoyi.Xidian.mapper.TaskDataGroupMapper;
import com.ruoyi.Xidian.mapper.TaskMapper;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class TaskListener {
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private TaskDataGroupMapper taskDataGroupMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RedisCache redisCache;

    @RabbitListener(queues = "simulation_task_queue")
    public void handleTask(TaskDataGroup taskDataGroup, Channel channel, Message message) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        // 1. 幂等检查：如果任务已经成功了，直接签收掉
        TaskDataGroup currentTaskDataGroup = taskDataGroupMapper.selectById(taskDataGroup.getId());
        if (TaskStatusEnum.SUCCESS.toString().equals(currentTaskDataGroup.getStatus())) {
            channel.basicAck(deliveryTag, false);
            return;
        }

        try {
            // 2. 更新状态为运行中
            taskDataGroup.setStatus(TaskStatusEnum.RUNNING.toString());
            taskDataGroupMapper.update(taskDataGroup);
            //执行仿真任务中的每个数据项
            Long startTime = taskDataGroup.getStartTimeMs();
            Long endTime = taskDataGroup.getEndTimeMs();
            List<TaskDataMetric> metricList = taskDataGroup.getMetricList();
            //调用仿真接口，获取仿真结果，模拟运算时间
            Thread.sleep(5000);
            // 4. 运算成功：先改库，再 Ack
            taskDataGroup.setStatus(TaskStatusEnum.SUCCESS.toString());
            // 5. 从 Redis 中减 1
            Integer taskGroups = redisCache.getCacheObject(taskDataGroup.getTaskId().toString());
            taskGroups--;
            redisCache.setCacheObject(taskDataGroup.getTaskId().toString(), taskGroups);
            if(taskGroups == 0){
                // 任务所有子任务都执行完成，标记为成功
                Task task = taskMapper.selectById(taskDataGroup.getTaskId());
                task.setStatus(TaskStatusEnum.SUCCESS.toString());
                taskMapper.update(task);
                redisCache.deleteObject(task.getId().toString());
            }
            taskDataGroupMapper.update(taskDataGroup);
            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            // 5. 运算失败：
            // 第三个参数 false 表示不重回原队列，配合“死信队列”使用
            int retryCount = getRetryCount(message);
            if(retryCount < 3){
                // 重试次数小于3次，重新入队
                message.getMessageProperties().setHeader("retry-count", retryCount + 1);
                channel.basicNack(deliveryTag, false, false);
            }else{
                // 重试次数大于等于3次，标记为失败
                taskDataGroup.setStatus(TaskStatusEnum.FAILED.toString());
                taskDataGroupMapper.update(taskDataGroup);
                rabbitTemplate.send("retry_exchange", "final_routing", message);
                channel.basicNack(deliveryTag, false, true);
                throw new ServiceException("任务执行失败，请稍后再试");
            }
        }
    }

    public int getRetryCount(Message message) {
        MessageProperties properties = message.getMessageProperties();
        Map<String, Object> headers = properties.getHeaders();
        Object count = headers.get("retry-count");
        if (count instanceof Integer) {
            return (Integer) count;
        }
        return 0;
    }
}
