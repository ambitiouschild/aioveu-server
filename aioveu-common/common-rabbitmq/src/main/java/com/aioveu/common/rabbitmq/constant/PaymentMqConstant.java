package com.aioveu.common.rabbitmq.constant;


/**
 * @ClassName: PaymentMqConstant
 * @Description TODO 支付 MQ 的「最终标准配置」
 *                          常量定义（强烈推荐）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/26 21:33
 * @Version 1.0
 **/

public interface PaymentMqConstant {

    String EXCHANGE = "payment.exchange";

    String QUEUE_SUCCESS = "payment.success.queue";
    String QUEUE_FAILED  = "payment.failed.queue";

    String RK_SUCCESS = "payment.success";
    String RK_FAILED  = "payment.failed";
}
