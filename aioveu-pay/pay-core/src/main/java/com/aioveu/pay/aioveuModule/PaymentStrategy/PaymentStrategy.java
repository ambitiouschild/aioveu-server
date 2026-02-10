package com.aioveu.pay.aioveuModule.PaymentStrategy;

import com.aioveu.pay.aioveuModule.model.vo.*;

/**
 * @ClassName: PaymentStrategy
 * @Description TODO 支付策略接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 17:49
 * @Version 1.0
 **/

public interface PaymentStrategy {

    PaymentParamsVO appPay(String paymentNo, PaymentRequestDTO request);

    boolean verifyCallback(PaymentCallbackDTO callback);

    PaymentStatusVO queryStatus(String paymentNo);

    boolean closePayment(String paymentNo);

    RefundResultVO refund(String refundNo, RefundRequestDTO request);
}
