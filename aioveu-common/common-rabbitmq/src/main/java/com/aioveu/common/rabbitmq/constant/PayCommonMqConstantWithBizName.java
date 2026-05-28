package com.aioveu.common.rabbitmq.constant;


import org.springframework.stereotype.Component;

/**
 * @ClassName: PayCommonMqConstantWithBizName
 * @Description TODO  改成 Spring Bean（最重要）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/28 20:59
 * @Version 1.0
 **/
@Component
public class PayCommonMqConstantWithBizName {

    public static final String SYSTEM_NAME = "aivoeu-common";

    public static class Exchange {
        public static final String PAYMENT = SYSTEM_NAME + ".payment.exchange";
        public static final String DLX     = SYSTEM_NAME + ".payment.success.dlx";
    }

    public static class Queue {
        public static final String SUCCESS = SYSTEM_NAME + ".payment.success.queue";
        public static final String FAILED  = SYSTEM_NAME + ".payment.failed.queue";
        public static final String DLQ     = SYSTEM_NAME + ".payment.success.queue.dlq";
    }

    public static class RoutingKey {
        public static final String SUCCESS = "payment.success";
        public static final String FAILED  = "payment.failed";
        public static final String DLQ     = SYSTEM_NAME + ".payment.success.queue";
    }
}
