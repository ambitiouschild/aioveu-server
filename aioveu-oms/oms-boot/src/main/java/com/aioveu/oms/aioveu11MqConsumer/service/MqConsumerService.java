package com.aioveu.oms.aioveu11MqConsumer.service;


import com.aioveu.oms.aioveu11MqConsumer.model.vo.OrderPaySuccessDTO;
import com.aioveu.common.rabbitmq.producer.model.payment.PaymentSuccessMessage;

/**
 * @ClassName: MqConsumerService
 * @Description TODO  创建MQ消费者服务
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/11 20:18
 * @Version 1.0
 **/

public interface MqConsumerService {




    /**
     * 处理支付成功消息
     * 消息体格式示例：
     * {
     *   "messageId": "PAY_20250316143025123_01_0001_456",
     *   "paymentNo": "PAY20250316143025",
     *   "orderNo": "ORDER20250316143025",
     *   "transactionId": "4200001972202503151234567890",
     *   "amount": 100.00,
     *   "channel": "wechat",
     *   "memberId": 1001,
     *   "paymentTime": "2025-03-16T14:30:25"
     * }
     *
     * 处理支付成功消息（字符串版本）
     *
     */
    boolean handlePaymentSuccess(String messageBody);



    /**
     * 处理支付成功消息（对象版本）
     */
    boolean handlePaymentSuccess(PaymentSuccessMessage message);

    /**
     * 处理支付成功消息（DTO版本）
     */
    boolean handlePaymentSuccess(OrderPaySuccessDTO paySuccess);

}
