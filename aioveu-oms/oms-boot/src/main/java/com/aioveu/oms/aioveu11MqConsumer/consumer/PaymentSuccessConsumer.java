package com.aioveu.oms.aioveu11MqConsumer.consumer;

import com.aioveu.oms.aioveu01Order.enums.OrderStatusEnum;
import com.aioveu.oms.aioveu01Order.model.entity.OmsOrder;
import com.aioveu.oms.aioveu01Order.service.app.OrderService;
import com.aioveu.oms.aioveu04OrderLog.service.OmsOrderLogService;
import com.aioveu.oms.aioveu08MqConsumeRecord.service.MqConsumeRecordService;
import com.aioveu.oms.aioveu11MqConsumer.service.MqConsumerService;
import com.aioveu.pay.model.PaymentSuccessMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.ZoneId;
import java.util.Date;

/**
 * @ClassName: PaymentSuccessConsumer
 * @Description TODO 订单服务 - 消费者
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/11 18:34
 * @Version 1.0
 **/


@Service
@Slf4j
// # 不再需要 consumer 配置，注解中已包含
@RocketMQMessageListener(
        topic = "${rocketmq.topics.payment-success:payment_success_topic}",
        consumerGroup = "${spring.application.name}-payment-consumer",
        selectorExpression = "*",  // 消费所有Tag
//        selectorExpression = "wechat_pay || alipay",  // 只消费微信和支付宝支付消息
        consumeMode = ConsumeMode.ORDERLY,  // 顺序消费
        messageModel = MessageModel.CLUSTERING,  // 集群模式
        consumeThreadMax = 20,  // 最大消费线程数
        consumeTimeout = 15L,  // 消费超时时间(分钟)
        suspendCurrentQueueTimeMillis = 1000,        // 消费失败挂起时间
        delayLevelWhenNextConsume = 0                // 消费失败延迟级别
)
@RequiredArgsConstructor  // Lombok 自动生成构造器
public class PaymentSuccessConsumer implements RocketMQListener<PaymentSuccessMessage> {

    // 使用 final 字段 + @RequiredArgsConstructor
    private final MqConsumerService mqConsumerService;
    // 不需要 @Autowired，Lombok 会自动生成带参构造器
    // Spring 会自动通过构造器注入

    @Autowired
    private ObjectMapper objectMapper;     // 使用 Jackson 的 ObjectMapper

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onMessage(PaymentSuccessMessage message) {
        try {
            // 1. 将 PaymentSuccessMessage 对象转为 JSON 字符串
            String messageBody = objectMapper.writeValueAsString(message);
            String messageId = message.getMessageId();
            String orderSn = message.getOrderNo();  // 从keys获取订单号

            long startTime = System.currentTimeMillis();
            log.info("收到支付成功消息: orderNo={}, messageId={}", orderSn, messageId);

            // 2. 调用处理服务
            boolean success1 = mqConsumerService.handlePaymentSuccess(messageBody);

            // 方案2：修改接口参数（推荐）
            //更好的设计是让 handlePaymentSuccess直接接收 PaymentSuccessMessage对象

            boolean success2 = mqConsumerService.handlePaymentSuccess(message);

            if (!success2) {
                log.error("处理消息失败: orderSn={}", orderSn);
                // 抛出异常让 RocketMQ 重试
                throw new RuntimeException("处理消息失败");
            }

            log.info("处理消息成功: orderNo={}", orderSn);

        } catch (Exception e) {
            log.error("处理消息异常", e);
            throw new RuntimeException("处理消息异常", e);
        }

    }
}
