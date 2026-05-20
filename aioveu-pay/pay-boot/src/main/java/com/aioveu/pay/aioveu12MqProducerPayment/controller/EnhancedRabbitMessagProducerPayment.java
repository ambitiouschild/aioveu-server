package com.aioveu.pay.aioveu12MqProducerPayment.controller;


import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu12MqProducerPayment.service.RabbitMessageServicePayment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @ClassName: PaymentMQProducer
 * @Description TODO 创建MQ生产者服务
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/11 19:33
 * @Version 1.0
 **/


@Service
@Slf4j
@RequiredArgsConstructor
public class EnhancedRabbitMessagProducerPayment {


    private final RabbitMessageServicePayment rabbitMessageServicePayment;
    /*
    *  发送支付成功消息并保存发送记录
    * */
    public boolean sendPaymentSuccessMessageAndSaveSendRecord(PayOrder payOrder, Map<String, String> params) {

        boolean result = rabbitMessageServicePayment.sendPaymentSuccessMessage(payOrder, params);

        return result;
    }


    /*
     *  发送支付失败消息并保存发送记录
     * */
    public boolean sendPaymentFailedMessageAndSaveSendRecord(PayOrder payOrder, Map<String, String> params) {

        boolean result = rabbitMessageServicePayment.sendPaymentFailedMessage( payOrder,  params);

        return result;
    }


}
