package com.aioveu.pay.aioveu12MqProducerPayment.service.impl;


import com.aioveu.common.rabbitmq.constant.PayCommonMqConstantWithBizName;
import com.aioveu.common.rabbitmq.producer.util.MessageIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * @ClassName: PayPrivateMessageServiceImpl
 * @Description TODO  私有队列 Producer（新增） 职责：只发 pay 服务内部用的消息
 * 例如：
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/28 19:42
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class PayPrivateMessageServiceImpl {

    private final String paymentExchange = PayCommonMqConstantWithBizName.Exchange.PAYMENT;
    private final String paymentSuccessRoutingKey = PayCommonMqConstantWithBizName.RoutingKey.SUCCESS;
    private final String paymentFailedRoutingKey = PayCommonMqConstantWithBizName.RoutingKey.FAILED;

    private final RabbitTemplate rabbitTemplate;
    private final MessageIdGenerator messageIdGenerator;

//    public boolean sendPayCloseMessage(String paymentNo) {
//        String messageId = messageIdGenerator.generatePaymentMessageId(paymentNo);
//
//        PayCloseMessage message = PayCloseMessage.builder()
//                .messageId(messageId)
//                .paymentNo(paymentNo)
//                .closeTime(LocalDateTime.now())
//                .build();
//
//        rabbitTemplate.convertAndSend(
//                payPrivateExchange,
//                payCloseRoutingKey,
//                message
//        );
//        return true;
//    }
}
