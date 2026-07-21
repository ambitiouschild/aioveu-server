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
//    @Async("bizExecutor")

    /*
        MQ 发送本身就是异步
        RabbitTemplate 非阻塞
        你已经在 Job / 回调线程里了
        再 @Async= 线程浪费 + Trace 丢失
* */
    @Override
    public void onPaid(String paymentNo) {

        log.info("【OrderBusinessProcessorImpl】处理订单支付, paymentNo={}", paymentNo);
        log.info("支付成功，开始处理业务,什么都不做，或者直接移除这个类，paymentNo={}", paymentNo);


    }



}
