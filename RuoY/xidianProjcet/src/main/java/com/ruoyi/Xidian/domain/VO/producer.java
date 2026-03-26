package com.ruoyi.Xidian.domain.VO;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class producer {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        // 1. 创建连接工厂，设置连接参数
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");      // RabbitMQ 服务地址
        factory.setPort(5672);             // 端口，默认 5672
        factory.setUsername("guest");      // 用户名，默认 guest
        factory.setPassword("guest");      // 密码，默认 guest
        factory.setVirtualHost("/");        // 虚拟主机，默认 /

        // 2. 创建连接
        try (Connection connection = factory.newConnection();
             // 3. 创建通道
             Channel channel = connection.createChannel()) {

            // 4. 声明队列（如果不存在则创建）
            // 参数：队列名、是否持久化、是否独占、是否自动删除、其他属性
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            String message = "Hello RabbitMQ!";
            // 5. 发送消息
            // 参数：交换机（空字符串表示默认交换机）、路由键（队列名）、消息属性、消息体（字节数组）
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
