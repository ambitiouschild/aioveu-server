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


    //把常量分组（一眼看出用途）
    // 1️补一个“消费者组 / 环境前缀”（防脏数据）
    String SYSTEM_NAME = "oms.";

    interface Exchange {
        String PAYMENT = "payment.exchange";
    }


    interface Queue {
        String SUCCESS = SYSTEM_NAME + "payment.success.queue";
        String FAILED  = SYSTEM_NAME + "payment.failed.queue";
        String DLQ     = SYSTEM_NAME + "payment.success.queue.dlq";
    }

    interface RoutingKey {
        String SUCCESS = "payment.success";
        String FAILED  = "payment.failed";
        String DLQ     = "payment.success.queue"; // ✅ 推荐
    }

    interface Dlx {
        String EXCHANGE = "payment.success.dlx";
    }

}
