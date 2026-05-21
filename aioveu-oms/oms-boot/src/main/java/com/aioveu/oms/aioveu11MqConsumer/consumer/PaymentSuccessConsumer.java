package com.aioveu.oms.aioveu11MqConsumer.consumer;

import com.aioveu.common.rabbitmq.config.RabbitConfig;
import com.aioveu.oms.aioveu11MqConsumer.service.MqConsumerService;
import com.aioveu.pay.model.PaymentSuccessMessage;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName: PaymentSuccessConsumer
 * @Description TODO 订单服务 - 消费者  标准 RabbitMQ Consumer（生产级）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/11 18:34
 * @Version 1.0
 **/


@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentSuccessConsumer{



    // 使用 final 字段 + @RequiredArgsConstructor
    private final MqConsumerService mqConsumerService;
    // 不需要 @Autowired，Lombok 会自动生成带参构造器
    // Spring 会自动通过构造器注入


    @Autowired
    private ObjectMapper objectMapper;     // 使用 Jackson 的 ObjectMapper

    @RabbitListener(
            queues = "#{'${rabbitmq.queue.payment-success}'}",
            containerFactory = "rabbitListenerContainerFactory"
    )
    @Transactional(rollbackFor = Exception.class)
    public void onMessage(Message message, Channel channel) throws IOException {

        long startTime = System.currentTimeMillis();
        String messageId = null;
        String orderSn = null;
        boolean success = false;

        try {
            String body = new String(message.getBody(), StandardCharsets.UTF_8);
            PaymentSuccessMessage msg = objectMapper.readValue(body, PaymentSuccessMessage.class);

            messageId = msg.getMessageId();
            orderSn = msg.getOrderNo();
            log.info("收到支付成功消息: orderNo={}, messageId={}", orderSn, messageId);

            // 2. 调用处理服务
            boolean result = mqConsumerService.handlePaymentSuccess(msg);
            if (!result) {
                throw new RuntimeException("处理消息失败");
            }

            success = true;
            if (success) {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            }

        } catch (Exception e) {
            log.error("处理消息异常", e);
            success = false;
            throw new IOException("处理消息异常", e);
        }finally {
            long costTime = System.currentTimeMillis() - startTime;

//            // 记录监控指标
//            orderConsumerMQMonitor.recordConsumeResult(orderSn, messageId,
//                    success, costTime, success ? null : "处理异常");
        }

    }
}
