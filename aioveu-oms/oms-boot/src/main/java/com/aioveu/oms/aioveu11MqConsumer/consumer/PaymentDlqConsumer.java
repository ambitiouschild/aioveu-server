package com.aioveu.oms.aioveu11MqConsumer.consumer;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @ClassName: PaymentDlqConsumer
 * @Description TODO DLQ 消息怎么人工处理（别漏）  DLQ Consumer（只做补偿）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/28 17:43
 * @Version 1.0
 **/
@Component
@Slf4j
public class PaymentDlqConsumer {

    @RabbitListener(queues = "#{payCommonMqConstantWithBizName.Queue.DLQ}")
    public void onDlq(Message message) {
        log.error("收到死信消息: {}", new String(message.getBody()));
        // 落表 / 人工工单 / 风控介入
    }
}
