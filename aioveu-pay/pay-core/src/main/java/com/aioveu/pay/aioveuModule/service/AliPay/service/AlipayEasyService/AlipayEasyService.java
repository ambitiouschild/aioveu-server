package com.aioveu.pay.aioveuModule.service.AliPay.service.AlipayEasyService;

import com.aioveu.pay.aioveuModule.model.vo.*;

/**
 * @ClassName: AlipayEasyService
 * @Description TODO AlipayEasyService
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 21:01
 * @Version 1.0
 **/

public interface AlipayEasyService {

    /**
     * APP支付
     */
    PaymentParamsVO appPay(PaymentRequestDTO request);


    /**
     * 网页支付
     */
    PaymentParamsVO pagePay(PaymentRequestDTO request);

    /**
     * 手机网站支付
     */
    PaymentParamsVO wapPay(PaymentRequestDTO request);

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
