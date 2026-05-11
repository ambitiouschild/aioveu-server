package com.aioveu.pay.aioveu12MqProducerPayment.service;


import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu10MqSendRecord.model.entity.MqSendRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @ClassName: MqMessageService
 * @Description TODO MQ消息服务类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/11 19:00
 * @Version 1.0
 **/

public interface MqMessageService extends IService<MqSendRecord> {


    /**
     * 发送支付成功消息
     */
    boolean sendPaymentSuccessMessage(PayOrder payOrder, Map<String, String> params);

    /**
     * 发送支付失败消息
     */
    void sendPaymentFailedMessage(PayOrder payOrder, Map<String, String> params);

    /**

}
