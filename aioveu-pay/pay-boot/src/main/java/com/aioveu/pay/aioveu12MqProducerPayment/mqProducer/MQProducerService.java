package com.aioveu.pay.aioveu12MqProducerPayment.mqProducer;


import com.aioveu.pay.aioveu10MqSendRecord.model.entity.MqSendRecord;
import com.aioveu.common.rabbitmq.producer.model.vo.RabbitBatchSendResult;
import com.aioveu.common.rabbitmq.producer.model.vo.RabbitSendRequest;
import com.aioveu.common.rabbitmq.producer.model.vo.RabbitSendResult;
import com.aioveu.common.rabbitmq.producer.model.payment.PaymentSuccessMessage;
import com.aioveu.pay.aioveu12MqProducerPayment.model.vo.SendPaymentMqDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @ClassName: MqMessageService
 * @Description TODO RabbitMQ消息发送服务
 *                      支持同步/异步发送、批量发送、延迟消息、顺序消息
 *                      第二层：内部共用一个“发送引擎”（收敛重复代码）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/11 19:00
 * @Version 1.0
 **/

public interface MQProducerService extends IService<MqSendRecord> {


    /**
     * 发送支付成功消息
     */
    boolean sendPaymentSuccessMessage(SendPaymentMqDTO dto);

    /**
     * 发送支付失败消息
     */
    boolean sendPaymentFailedMessage(SendPaymentMqDTO dto);


    /**
     * 批量发送消息
     */
    RabbitBatchSendResult batchSend(List<PaymentSuccessMessage> messages);


    /**
     * 发送单条消息（完整功能版）（同步）
     *
     * @param request 发送请求
     * @return 发送结果
     */
    RabbitSendResult sendSingleMessage(RabbitSendRequest request);
}
