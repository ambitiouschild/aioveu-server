package com.aioveu.pay.aioveu12MqProducerPayment.Publisher;


import com.aioveu.common.rabbitmq.producer.util.MessageIdGenerator;
import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu11MqCompensationTask.service.MqCompensationTaskService;
import com.aioveu.pay.aioveu12MqProducerPayment.enums.PaymentMqBizType;
import com.aioveu.pay.aioveu12MqProducerPayment.model.vo.SendPaymentMqDTO;
import com.aioveu.pay.aioveu12MqProducerPayment.mqProducer.MQProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


/**
 * @ClassName: PaymentEventPublisher
 * @Description TODO 定义一个「支付事件发布器」
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/21 18:34
 * @Version 1.0
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentEventPublisher {


    private final MessageIdGenerator messageIdGenerator;
    private final MQProducerService MQProducerService;
    private final MqCompensationTaskService mqCompensationTaskService;

    /**
     * 支付成功事件（只发一次）
     */
    public void publishPaymentSuccess(PayOrder payOrder) {


        long startTime = System.currentTimeMillis();

        SendPaymentMqDTO dto = SendPaymentMqDTO.builder()
                .messageId(messageIdGenerator.generatePaymentMessageId(payOrder.getPaymentNo()))
                .paymentNo(payOrder.getPaymentNo())
                .omsOrderSn(payOrder.getOrderNo())
                .transactionId(payOrder.getThirdTransactionNo())
                .paymentAmount(payOrder.getPaymentAmount())
                .channel(payOrder.getPaymentChannel())
                .paymentTime(payOrder.getPaymentTime())
                .manualSend(false)
                .messageType(PaymentMqBizType.PAYMENT_SUCCESS)
                .build();


            boolean mqSuccess = MQProducerService.sendPaymentSuccessMessage(dto);

            // 如果MQ发送失败，记录到补偿表
            if (!mqSuccess) {
                log.warn("【PaymentEventPublisher】MQ发送失败，记录到补偿表: paymentNo={}", payOrder.getPaymentNo());
                mqCompensationTaskService.saveToCompensation(payOrder, "MQ_SEND_FAILED");
            }

            // 7. 记录处理时间
            long costTime = System.currentTimeMillis() - startTime;
            log.info("【PaymentEventPublisher】支付成功处理完成: paymentNo={}, 订单号={}, 微信订单号={}, 耗时={}ms",
                    payOrder.getPaymentNo(), payOrder.getOrderNo(), payOrder.getThirdTransactionNo(), costTime);
    }



    /**
     * 支付失败事件（只发一次）
     */
    public void publishPaymentFailure(PayOrder payOrder) {

        long startTime = System.currentTimeMillis();

        SendPaymentMqDTO dto = SendPaymentMqDTO.builder()
                .messageId(messageIdGenerator.generatePaymentMessageId(payOrder.getPaymentNo()))
                .paymentNo(payOrder.getPaymentNo())
                .omsOrderSn(payOrder.getOrderNo())
                .transactionId(payOrder.getThirdTransactionNo())
                .paymentAmount(payOrder.getPaymentAmount())
                .channel(payOrder.getPaymentChannel())
                .paymentTime(payOrder.getPaymentTime())
                .manualSend(false)
                .messageType(PaymentMqBizType.PAYMENT_SUCCESS)
                .build();


        boolean mqSuccess = MQProducerService.sendPaymentFailedMessage(dto);

        // 如果MQ发送失败，记录到补偿表
        if (!mqSuccess) {
            log.warn("【PaymentEventPublisher】支付失败事件MQ发送失败，记录到补偿表: paymentNo={}", payOrder.getPaymentNo());
            mqCompensationTaskService.saveToCompensation(payOrder, "MQ_SEND_FAILED");
        }

        // 7. 记录处理时间
        long costTime = System.currentTimeMillis() - startTime;
        log.info("【PaymentEventPublisher】支付失败处理完成: paymentNo={}, 订单号={}, 微信订单号={}, 耗时={}ms",
                payOrder.getPaymentNo(), payOrder.getOrderNo(), payOrder.getThirdTransactionNo(), costTime);
    }





}
