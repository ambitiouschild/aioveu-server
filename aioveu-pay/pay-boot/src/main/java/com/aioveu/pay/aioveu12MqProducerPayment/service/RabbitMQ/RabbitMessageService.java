package com.aioveu.pay.aioveu12MqProducerPayment.service.RabbitMQ;


import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu10MqSendRecord.model.entity.MqSendRecord;
import com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RocketMQ.RocketBatchSendResult;
import com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ.RabbitSendResult;
import com.aioveu.pay.aioveu12MqProducerPayment.model.vo.PaymentSuccessMessage;
import com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ.RabbitSendRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: MqMessageService
 * @Description TODO RabbitMQ消息发送服务
 *                      支持同步/异步发送、批量发送、延迟消息、顺序消息
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/11 19:00
 * @Version 1.0
 **/

public interface RabbitMessageService extends IService<MqSendRecord> {


}
