package com.aioveu.pay.aioveu12MqProducerPayment.service.impl;


import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu10MqSendRecord.enums.SendStatusEnum;
import com.aioveu.pay.aioveu10MqSendRecord.mapper.MqSendRecordMapper;
import com.aioveu.pay.aioveu10MqSendRecord.model.entity.MqSendRecord;
import com.aioveu.pay.aioveu10MqSendRecord.service.MqSendRecordService;
import com.aioveu.pay.aioveu12MqProducerPayment.model.vo.PaymentFailedMessage;
import com.aioveu.pay.aioveu12MqProducerPayment.model.vo.PaymentSuccessMessage;
import com.aioveu.pay.aioveu12MqProducerPayment.service.MqMessageService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.rocketmq.client.producer.SendResult;  // ✅
import java.time.LocalDateTime;
import java.util.*;

/**
 * @ClassName: MqSendRecordServiceImpl
 * @Description TODO MQ消息发送记录服务实现类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 21:48
 * @Version 1.0
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class MqMessageServiceImpl extends ServiceImpl<MqSendRecordMapper, MqSendRecord> implements MqMessageService {



    private final MqSendRecordMapper mqSendRecordMapper;
    private final RocketMQTemplate rocketMQTemplate;

    private final MqSendRecordService mqSendRecordService;

    @Value("${rocketmq.topic.payment-success:payment_success_topic}")
    private String paymentSuccessTopic;

    /**
     * 发送支付成功消息
     */
    @Override
    public boolean sendPaymentSuccessMessage(PayOrder payOrder, Map<String, String> params) {


        try {

            // 构建消息
            PaymentSuccessMessage message = PaymentSuccessMessage.builder()
                    .messageId(UUID.randomUUID().toString())
                    .paymentNo(payOrder.getPaymentNo())
                    .orderNo(payOrder.getOrderNo())
                    .transactionId(params.get("transaction_id"))
                    .amount(payOrder.getPaymentAmount())
                    .channel(payOrder.getPaymentChannel())
                    .paymentTime(LocalDateTime.now())
                    .memberId(payOrder.getUserId())
                    .build();

            // 保存发送记录
            String messageId = mqSendRecordService.saveMqSendRecord(
                    "payment_success_topic",
                    "wechat_pay",
                    payOrder.getPaymentNo(),  // 使用订单号保证顺序
                    message
            );



            // 发送顺序消息，确保同一订单的消息顺序
            SendResult sendResult = rocketMQTemplate.syncSendOrderly(
                    paymentSuccessTopic + ":wechat_pay",
                    MessageBuilder
                            .withPayload(message)
                            .setHeader(MessageConst.PROPERTY_KEYS, payOrder.getOrderNo())
                            .build(),
                    payOrder.getOrderNo(),  // 使用订单号作为Sharding Key  // 使用订单号保证顺序
                    3000  // 3秒超时
            );

            // 更新发送状态
            Boolean  updateSendStatus =  mqSendRecordService.updateSendStatus(messageId, SendStatusEnum.SUCCESS, null);



            log.info("【MQ发送】支付成功消息发送成功: paymentNo={}, messageId={}",
                    payOrder.getPaymentNo(), messageId);

            return updateSendStatus;

        } catch (Exception e) {
            log.error("发送支付成功消息异常: paymentNo={}", payOrder.getPaymentNo(), e);
            // 可以记录到补偿表，定时任务重试

            // 记录发送失败，后续补偿任务会重试
            mqSendRecordService.updateSendStatus(
                    UUID.randomUUID().toString(),
                    SendStatusEnum.FAILED,
                    e.getMessage()
            );

        }

    }


    /**
     * 发送支付失败消息
     */
    @Override
    public void sendPaymentFailedMessage(PayOrder payOrder, Map<String, String> params) {
        PaymentFailedMessage message = PaymentFailedMessage.builder()
                .paymentNo(payOrder.getPaymentNo())
                .orderNo(payOrder.getOrderNo())
                .errorCode(params.get("err_code"))
                .errorMsg(params.get("err_code_des"))
                .channel(payOrder.getPaymentChannel())
                .build();

        rocketMQTemplate.syncSend("payment-failed-topic", message);
    }




}
