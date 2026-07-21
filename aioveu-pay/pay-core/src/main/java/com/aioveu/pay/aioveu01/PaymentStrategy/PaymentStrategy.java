package com.aioveu.pay.aioveu01.PaymentStrategy;

import com.aioveu.pay.model.aioveuPayQueryResultAdapter.PaymentStatusDTO;
import com.aioveu.pay.model.aioveuPayment.*;
import com.aioveu.pay.model.aioveuPayment.request.PaymentRequestPayToTPPDTO;

/**
 * @ClassName: PaymentStrategy
 * @Description TODO 支付策略接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 17:49
 * @Version 1.0
 **/

public interface PaymentStrategy {

    PaymentParamsVO appPay(String paymentNo, PaymentRequestPayToTPPDTO request);

    boolean verifyCallback(PaymentCallbackDTO callback);


    // ✅ 内部统一模型 适用于多种支付
    PaymentStatusDTO queryStatus(String paymentNo);

    boolean closePayment(String paymentNo);

    com.aioveu.pay.model.aioveuPayment.RefundResultVO refund(String refundNo, RefundRequestDTO request);
}
