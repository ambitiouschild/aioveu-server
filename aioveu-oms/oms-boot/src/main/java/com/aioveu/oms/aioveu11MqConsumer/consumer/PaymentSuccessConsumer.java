package com.aioveu.oms.aioveu11MqConsumer.consumer;

import com.aioveu.common.rabbitmq.config.RabbitConfig;
import com.aioveu.common.web.exception.BizException;
import com.aioveu.oms.aioveu08MqConsumeRecord.service.MqConsumeRecordService;
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
 *                          消费者入口（@RabbitListener）
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
    private final MqConsumeRecordService mqConsumeRecordService;

    @Autowired
    private ObjectMapper objectMapper;     // 使用 Jackson 的 ObjectMapper

    @RabbitListener(
            queues = "payment.success.queue",
            concurrency = "4-8",   // ✅ 并发控制
            ackMode = "MANUAL"     // ✅ 手动 ACK（非常重要）
    )
    @Transactional(rollbackFor = Exception.class)
    public void onMessage(Message message, Channel channel) throws IOException {

        long startTime = System.currentTimeMillis();
        String messageId = null;
        String orderNo = null;
        boolean success = false;

        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            String body = new String(message.getBody(), StandardCharsets.UTF_8);
            PaymentSuccessMessage msg = objectMapper.readValue(body, PaymentSuccessMessage.class);

            messageId = msg.getMessageId();
            orderNo = msg.getOrderNo();

            log.info("【Pay-Consumer】收到支付成功消息, orderNo={}, messageId={}", orderNo, messageId);

            // ✅ 1. 幂等校验（最重要）
            if (mqConsumeRecordService.isConsumed(messageId)) {
                log.warn("【Pay-Consumer】消息已消费，直接 ACK, messageId={}", messageId);
                channel.basicAck(deliveryTag, false);
                return;
            }

            // ✅ 2. 处理业务（不要在这里抛系统异常）
            mqConsumerService.handlePaymentSuccess(msg);

            // ✅ 3. 标记已消费
            mqConsumeRecordService.markConsumed(messageId, orderNo);

            // ✅ 4. 手动 ACK
            channel.basicAck(deliveryTag, false);
            success = true;

        } catch (BizException e) {
            // ❌ 业务异常 → 不重试，进 DLQ
            log.error("【Pay-Consumer】业务异常, messageId={}", messageId, e);
            rejectToDlq(channel, deliveryTag);

        } catch (Exception e) {
            // ❌ 系统异常 → NACK 重试
            log.error("【Pay-Consumer】系统异常，等待重试, messageId={}", messageId, e);
            nackAndRequeue(channel, deliveryTag);

        } finally {
            long costTime = System.currentTimeMillis() - startTime;
            // 监控指标
        }
    }

    private void rejectToDlq(Channel channel, long deliveryTag) throws IOException {
        channel.basicReject(deliveryTag, false); // requeue=false → DLQ
    }

    private void nackAndRequeue(Channel channel, long deliveryTag) throws IOException {
        channel.basicNack(deliveryTag, false, true); // requeue=true
    }
}
