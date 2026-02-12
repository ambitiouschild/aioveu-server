package com.aioveu.pay.aioveuModule.service.MockPay.service;

import com.aioveu.pay.aioveuModule.model.vo.*;

/**
 * @ClassName: MockPayService
 * @Description TODO 模拟支付服务接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/11 18:07
 * @Version 1.0
 **/

public interface MockPayService {

    /**
     * 模拟微信支付
     */
    PaymentParamsVO jsapiPay(PaymentRequestDTO request);

    /**
     * 模拟支付宝支付
     */
    PaymentParamsVO appPay(PaymentRequestDTO request);

    /**
     * 模拟余额支付
     */
    PaymentParamsVO h5Pay(PaymentRequestDTO request);

    /**
     * 查询订单状态
     */
    PaymentStatusVO queryPayment(String paymentNo);

    /**
     * 退款
     */
    RefundResultVO refund(RefundRequestDTO request);

    /**
     * 关闭订单
     */
    boolean closePayment(String paymentNo);

}
