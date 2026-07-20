package com.aioveu.pay.aioveu10MqSendRecord.Job;


import com.aioveu.common.enums.pay.PaymentSceneEnum;
import com.aioveu.common.rabbitmq.enums.SendStatusEnum;
import com.aioveu.common.rabbitmq.producer.model.payment.PaymentMessage;
import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu01PayOrder.service.PayOrderService;
import com.aioveu.pay.aioveu10MqSendRecord.model.entity.MqSendRecord;
import com.aioveu.pay.aioveu10MqSendRecord.service.MqSendRecordService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName: MqSendRetryJob
 * @Description TODO MqSendRetryJob 补偿 Job（单独类）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/20 21:56
 * @Version 1.0
 * @Param
 * @return
 **/

@Component
@Slf4j
@AllArgsConstructor
public class MqSendRetryJob {

    @Resource
    private MqSendRecordService mqSendRecordService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    private final PayOrderService payOrderService;

    @Scheduled(cron = "0 */1 * * * ?")
    public void retryUnsentEvents() {

        List<MqSendRecord> list = mqSendRecordService.list(
                Wrappers.<MqSendRecord>lambdaQuery()
                        .eq(MqSendRecord::getSendStatus, SendStatusEnum.FAILED)
                        .eq(MqSendRecord::getPaymentScene, PaymentSceneEnum.ORDER.getValue())
                        .lt(MqSendRecord::getRetryCount, 5)
                        .le(MqSendRecord::getNextRetryTime, LocalDateTime.now())
        );

        for (MqSendRecord record : list) {
            try {
                PaymentMessage msg = buildMessage(record);

                rabbitTemplate.convertAndSend(
                        record.getExchange(),
                        record.getRoutingKey(),
                        msg
                );

                mqSendRecordService.markSent(record.getBizId());

            } catch (Exception e) {
                log.error("MQ补偿发送失败, paymentNo={}", record.getBizId(), e);
                mqSendRecordService.increaseRetryCount(record.getId());
            }
        }
    }

    private PaymentMessage buildMessage(MqSendRecord record) {
        PayOrder order = payOrderService.getByPaymentNo(record.getBizId());
        return PaymentMessage.builder()
                .paymentNo(order.getPaymentNo())
                .paymentSceneEnum(PaymentSceneEnum.ORDER)
                .thirdTransactionNo(order.getThirdTransactionNo())
                .amount(order.getPaymentAmount())
                .successTime(order.getPaymentTime())
                .eventTime(LocalDateTime.now())
                .build();
    }
}
