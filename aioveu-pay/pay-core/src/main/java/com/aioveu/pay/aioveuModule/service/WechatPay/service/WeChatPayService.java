package com.aioveu.pay.aioveuModule.service.WechatPay.service;

import com.aioveu.pay.aioveuModule.model.vo.*;

/**
 * @ClassName: PaymentService
 * @Description TODO 微信支付服务接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 18:45
 * @Version 1.0
 **/

public interface WeChatPayService {

    /**
     * JSAPI支付（小程序/公众号）
     */
    PaymentParamsVO jsapiPay(PaymentRequestDTO request);


    /**
     * Native支付（扫码支付）
     */
//    PaymentParamsVO nativePay(PaymentRequestDTO request);

    /**
     * App支付
     */
    PaymentParamsVO appPay(PaymentRequestDTO request);

    /**
     * H5支付
     */
    PaymentParamsVO h5Pay(PaymentRequestDTO request);

    /**
     * 查询支付结果
     */
    PaymentStatusVO queryPayment(String paymentNo);

    /**
     * 关闭订单
     */
    boolean closePayment(String paymentNo);

    /**
     * 申请退款
     */
    RefundResultVO refund(RefundRequestDTO request);
}
