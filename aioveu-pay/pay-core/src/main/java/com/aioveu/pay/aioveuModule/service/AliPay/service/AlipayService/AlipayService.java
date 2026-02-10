package com.aioveu.pay.aioveuModule.service.AliPay.service.AlipayService;

import com.aioveu.pay.aioveuModule.model.vo.*;

/**
 * @ClassName: AlipayService
 * @Description TODO  支付宝支付服务接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 20:48
 * @Version 1.0
 **/

public interface AlipayService {

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
