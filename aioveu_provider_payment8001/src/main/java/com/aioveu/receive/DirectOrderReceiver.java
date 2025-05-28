package com.aioveu.receive;

import com.aioveu.config.mq.DirectMqConfig;
import com.aioveu.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

/**
 * @description 直连交换消息接收
 * @author: 雒世松
 * @date: 2025/6/19 0019 22:45
 */
@Slf4j
@Component
@RabbitListener(queues = DirectMqConfig.DIRECT_ORDER_QUEUE)
public class DirectOrderReceiver {

    @Autowired
    private OrderService orderService;
 
    @RabbitHandler
    public void process(Map<String, Object> message) {
        log.info("DirectReceiver消费者收到消息  : " + message.toString());
        if (message.get("orderId") != null) {
            orderService.toUsed(Collections.singletonList(message.get("orderId") + ""));
        }
    }
 
}