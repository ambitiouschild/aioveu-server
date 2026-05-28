package com.aioveu.pay.aioveu12MqProducerPayment.constant;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName: OrderMqConstant
 * @Description TODO MQ 常量类（Spring Bean）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/28 19:31
 * @Version 1.0
 **/

@Component
@Getter
@Slf4j
public class PayMqConstant {

    @Value("${spring.application.name}.")
    private String systemName;

    /* ================= Exchange ================= */

    public String exchangePayment() {
        return systemName + "payment.exchange";
    }

    public String dlxExchange() {
        return systemName + "payment.success.dlx";

    }

    /* ================= Queue ================= */


    public String queueSuccess() {
        return systemName + "payment.success.queue";
    }

    public String queueFailed() {
        return systemName + "payment.failed.queue";
    }

    public String queueDlq() {
        return systemName + "payment.success.queue.dlq";
    }


    /* ================= RoutingKey ================= */

    public String rkSuccess() {
        return "payment.success";
    }

    public String rkFailed() {
        return "payment.failed";
    }

    public String rkDlq() {
        return systemName + "payment.success.queue";
    }
}
