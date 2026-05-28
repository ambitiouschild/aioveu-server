package com.aioveu.oms.aioveu11MqConsumer.consumer;

import com.aioveu.common.web.exception.BizException;
import com.aioveu.oms.aioveu08MqConsumeRecord.service.MqConsumeRecordService;
import com.aioveu.oms.aioveu11MqConsumer.service.MqConsumerService;
import com.aioveu.pay.model.PaymentSuccessMessage;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
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

    @Value("${spring.application.name}")
    private String consumerGroup;

    @RabbitListener(
            queues = "#{PayCommonMqConstantWithBizName.Queue.Success}",
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


            log.info("✅ Consumer 只干一件事：收消息 + ACK");
            log.info("✅ RabbitMQ Consumer\n" +
                    "   └── 只负责：反序列化 + ACK");


            // ✅ 2. 处理业务（不要在这里抛系统异常） ✅ 业务处理（不含 ACK）
            mqConsumerService.handlePaymentSuccess(msg);


            // ✅ 4. 手动 ACK ✅ 成功 → ACK
            channel.basicAck(deliveryTag, false);
            success = true;
            log.info("同一条消息发 2 次\n" +
                    "\n" +
                    "第 2 次直接 ACK，不处理订单");

        } catch (BizException e) {
            // ❌ 业务异常 → 不重试，进 DLQ
            log.error("【Pay-Consumer】业务异常, messageId={}", messageId, e);
            rejectToDlq(channel, deliveryTag);

        } catch (Exception e) {
            // ❌ 系统异常 → NACK 重试
            log.error("【Pay-Consumer】系统异常，等待重试, messageId={}", messageId, e);
            nackAndRequeue(channel, deliveryTag, messageId, orderNo);

        } finally {
            long costTime = System.currentTimeMillis() - startTime;
            // 监控指标
        }
    }

    /**
     * ❌ 业务异常 → 直接进 DLQ
     */
    private void rejectToDlq(Channel channel, long deliveryTag) throws IOException {
        channel.basicReject(deliveryTag, false); // requeue=false → DLQ
        log.info("❌【业务异常 】 不重试，直接死信");
    }

    /**
     * ⚠️ 系统异常 → 重试 → 超过 3 次进 DLQ
     */
    private void nackAndRequeue(
            Channel channel,
            long deliveryTag,
            String messageId,
            String bizKey
    ) throws IOException {


        int retryCount =
                mqConsumeRecordService.getRetryCount(messageId, consumerGroup);

        if (retryCount >= 3) {
            log.error("【Pay-Consumer】超过最大重试次数，进入 DLQ, messageId={}", messageId);
            channel.basicReject(deliveryTag, false); // DLQ
            return;
        }

        // 增加重试次数
        mqConsumeRecordService.incrementRetryCount(messageId, consumerGroup, bizKey);
        channel.basicNack(deliveryTag, false, true); // 重试
        log.info("系统异常 → 重试 → 超过后进 DLQ,RabbitMQ 会自动把 reject 的消息转到 DLQ");


    }

}
