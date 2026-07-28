package com.aioveu.pay.aioveu00Payment.service;


import com.aioveu.common.core.enums.pay.CallbackTriggerSourceEnum;
import com.aioveu.common.core.enums.pay.PaymentStatusEnum;

import java.time.LocalDateTime;

/**
 * @ClassName: PayOrderSuccessHandler
 * @Description TODO 支付确认成功后的统一处理
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/7/25 23:12
 * @Version 1.0
 **/


public interface PayOrderSuccessHandlerService {

    /**
     * 统一处理支付成功
     *
     * @param paymentNo     支付单号
     * @param transactionId 第三方交易号
     * @param paidTime      支付时间
     * @param rawParams     原始参数（微信回调传 Map，轮询传 WechatPayQueryResult，Job 传查库结果）
     * @param source        来源标识（"WECHAT_CALLBACK" / "POLLING" / "JOB"）
     */

    void handlePaySuccess(
            String paymentNo,
            String transactionId,
            LocalDateTime paidTime,
            Object rawParams,
            CallbackTriggerSourceEnum source
    );

    /**
     * 处理支付失败/关闭（终态但不是 PAID）
     */
    void handlePayFail(
            String paymentNo,
            PaymentStatusEnum targetStatus,
            CallbackTriggerSourceEnum source);
}
