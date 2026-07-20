package com.aioveu.pay.aioveu00Payment.Processor.Impl;


import com.aioveu.common.enums.pay.PaymentSceneEnum;
import com.aioveu.common.rabbitmq.producer.model.payment.PaymentMessage;
import com.aioveu.pay.aioveu00Payment.Processor.BusinessProcessor;
import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu01PayOrder.service.PayOrderService;
import com.aioveu.pay.aioveu10MqSendRecord.service.MqSendRecordService;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @ClassName: OrderBusinessProcessorImpl
 * @Description TODO 订单业务处理器实现（多个）
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/7/20 19:31
 * @Version 1.0
 **/

@Service
@Slf4j
@AllArgsConstructor
public class OrderBusinessProcessorImpl implements BusinessProcessor {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Resource
    private MqSendRecordService mqSendRecordService;

    private final PayOrderService payOrderService;

    @Override
    public boolean supports(PaymentSceneEnum scene) {
        return scene == PaymentSceneEnum.ORDER;
    }

    /**
     * ✅ RabbitMQ：支付成功事件发送
     */
    /**
     * ✅ 只负责：发送支付成功事件
     */
    //@Async放在「BusinessProcessor 的具体实现类」里
    @Async("bizExecutor")
    @Override
    public void onPaid(String paymentNo) {

        log.info("处理订单支付, paymentNo={}", paymentNo);
        log.info("支付成功，开始处理业务，paymentNo={}", paymentNo);

        // 1. 查订单
        // 2. 发货 / 开通权益 / 记账
        // 3. 发 MQ / 通知用户

        //一个“企业级标准模板”（直接可用）

        String traceId = MDC.get("traceId");
        MDC.put("traceId", traceId);


        try {

            //判断支付成功事件是否已发送
            if (mqSendRecordService.bizEventAlreadySent(paymentNo)) {
                log.info("支付成功事件已发送，跳过, paymentNo={}", paymentNo);
                return;
            }

            PaymentMessage msg = buildMessage(paymentNo);

            rabbitTemplate.convertAndSend(
                    "pay.exchange",
                    "pay.success.order",
                    msg
            );

            //标记支付成功事件是否已发送
            mqSendRecordService.markBizEventSent(paymentNo, PaymentSceneEnum.ORDER);

            log.info("支付成功事件发送RabbitMQ成功, paymentNo={}", paymentNo);

        } catch (Exception e) {
            log.error("支付成功事件发送RabbitMQ失败, paymentNo={}", paymentNo, e);
            mqSendRecordService.saveRetryRecord(paymentNo, e);
        } finally {
            MDC.clear();
        }

    }


    private PaymentMessage buildMessage(String paymentNo) {
        // 这里只查“支付单”，不查业务订单
        PayOrder order = payOrderService.getByPaymentNo(paymentNo);

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
