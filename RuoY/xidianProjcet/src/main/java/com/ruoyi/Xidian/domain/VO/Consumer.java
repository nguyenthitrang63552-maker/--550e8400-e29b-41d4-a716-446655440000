package com.ruoyi.Xidian.domain.VO;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class Consumer {

    @Autowired
    static RabbitTemplate rabbitTemplate;

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        rabbitTemplate.convertAndSend(QUEUE_NAME, "Hello RabbitMQ!");
    }
}