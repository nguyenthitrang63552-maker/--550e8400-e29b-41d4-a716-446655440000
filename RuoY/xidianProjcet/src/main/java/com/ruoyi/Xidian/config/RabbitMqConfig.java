package com.ruoyi.Xidian.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMqConfig {
    //创建队列
    @Bean
    public Queue simulationTaskQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "retry_exchange");
        arguments.put("x-dead-letter-routing-key", "retry_routing");
        return new Queue("simulation_task_queue", true, false, false, arguments);
    }
    //重试队列
    @Bean
    public Queue simulationTaskRetryQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl", 10000);//10秒
        arguments.put("x-dead-letter-exchange", "simulation_task_exchange");//死信指向原交换机
        arguments.put("x-dead-letter-routing-key", "simulation_task_routing");
        return new Queue("simulation_task_retry_queue", true, false, false, arguments);
    }
    //死信队列
    @Bean
    public Queue finalQueue(){
        return new Queue("final_queue", true);
    }
    //创建交换机
    @Bean
    public Exchange simulationTaskExchange() {
        return new DirectExchange("simulation_task_exchange");
    }
    //创建交换机
    @Bean
    public Exchange retryExchange() {
        return new DirectExchange("retry_exchange");
    }
    //创建绑定关系
    @Bean
    public Binding simulationTaskBinding() {
        return BindingBuilder.bind(simulationTaskQueue())
                .to(simulationTaskExchange())
                .with("simulation_task_routing").noargs();
    }
    //创建绑定关系
    @Bean
    public Binding retryBinding() {
        return BindingBuilder.bind(simulationTaskRetryQueue())
                .to(retryExchange())
                .with("retry_routing").noargs();
    }
    //创建绑定关系
    @Bean
    public Binding finalBinding() {
        return BindingBuilder.bind(finalQueue())
                .to(retryExchange())
                .with("final_routing").noargs();
    }
}
