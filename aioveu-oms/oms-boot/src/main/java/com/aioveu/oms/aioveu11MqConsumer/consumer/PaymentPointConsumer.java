package com.aioveu.oms.aioveu11MqConsumer.consumer;


import com.aioveu.pay.model.PaymentSuccessMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @ClassName: PaymentPointConsumer
 * @Description TODO 积分服务 - 消费者
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/11 18:39
 * @Version 1.0
 **/
@Service
@Slf4j
@RocketMQMessageListener(
        topic = "${rocketmq.payment.success.topic:payment-success-topic}",
        consumerGroup = "${spring.application.name}-payment-consumer",
//        selectorExpression = "wechat_pay || alipay",  // 只消费微信和支付宝支付消息
        consumeMode = ConsumeMode.ORDERLY,  // 顺序消费
        messageModel = MessageModel.CLUSTERING,  // 集群模式
        consumeThreadMax = 20,  // 最大消费线程数
        consumeTimeout = 15L,  // 消费超时时间(分钟)
        suspendCurrentQueueTimeMillis = 1000,        // 消费失败挂起时间
        delayLevelWhenNextConsume = 0                // 消费失败延迟级别
)
public class PaymentPointConsumer implements RocketMQListener<PaymentSuccessMessage> {

    @Autowired
//    private MemberPointService pointService;

    @Override
    public void onMessage(PaymentSuccessMessage message) {
        log.info("积分服务收到支付成功消息: orderNo={}", message.getOrderNo());

        try {
            // 发放积分（支付金额的10%）
            BigDecimal points = message.getAmount()
                    .multiply(new BigDecimal("0.1"))
                    .setScale(0, RoundingMode.DOWN);

//            pointService.grantPoints(
//                    message.getMemberId(),
//                    points.intValue(),
//                    "支付奖励",
//                    message.getOrderNo()
//            );

        } catch (Exception e) {
            log.error("发放积分异常: orderNo={}", message.getOrderNo(), e);
        }
    }
}
