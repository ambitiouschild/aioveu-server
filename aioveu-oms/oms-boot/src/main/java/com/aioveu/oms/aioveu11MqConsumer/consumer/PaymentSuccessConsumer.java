package com.aioveu.oms.aioveu11MqConsumer.consumer;

import com.aioveu.common.rabbitmq.config.RabbitConfig;
import com.aioveu.oms.aioveu11MqConsumer.service.MqConsumerService;
import com.aioveu.pay.model.PaymentSuccessMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @ClassName: PaymentSuccessConsumer
 * @Description TODO 订单服务 - 消费者
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/11 18:34
 * @Version 1.0
 **/


@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentSuccessConsumer{


    private final RabbitConfig rabbitConfig;

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
    public void onMessage(PaymentSuccessMessage message) {

        long startTime = System.currentTimeMillis();
        boolean success = false;
        String messageId = message.getMessageId();
        String orderSn = message.getOrderNo();  // 从keys获取订单号

        try {
            // 1. 将 PaymentSuccessMessage 对象转为 JSON 字符串
            String messageBody = objectMapper.writeValueAsString(message);


            log.info("收到支付成功消息: orderNo={}, messageId={}", orderSn, messageId);

            // 2. 调用处理服务
            boolean success1 = mqConsumerService.handlePaymentSuccess(messageBody);

            // 方案2：修改接口参数（推荐）
            //更好的设计是让 handlePaymentSuccess直接接收 PaymentSuccessMessage对象

            boolean success2 = mqConsumerService.handlePaymentSuccess(message);

            success = true;  // 如果处理成功

            if (!success2) {
                log.error("处理消息失败: orderSn={}", orderSn);
                // 抛出异常让 RocketMQ 重试
                throw new RuntimeException("处理消息失败");
            }

            log.info("处理消息成功: orderNo={}", orderSn);

        } catch (Exception e) {
            log.error("处理消息异常", e);
            success = false;
            throw new RuntimeException("处理消息异常", e);
        }finally {
            long costTime = System.currentTimeMillis() - startTime;

//            // 记录监控指标
//            orderConsumerMQMonitor.recordConsumeResult(orderSn, messageId,
//                    success, costTime, success ? null : "处理异常");
        }

    }
}
