package com.aioveu.pay.aioveu01.service.WechatPay.service;

import com.aioveu.pay.model.aioveuPayment.PaymentParamsVO;
import com.aioveu.pay.model.aioveuPayment.RefundRequestDTO;
import com.aioveu.pay.model.aioveuPayQueryResultAdapter.WechatPayQueryResult;
import com.aioveu.pay.model.aioveuPayment.request.PaymentRequestPayToTPPDTO;

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
    PaymentParamsVO jsapiPay(PaymentRequestPayToTPPDTO request);


//    /**
//     * Native支付（扫码支付）
//     */
//    PaymentParamsVO nativePay(PaymentRequestPayToTPPDTO request);

    /**
     * App支付
     */
    PaymentParamsVO appPay(PaymentRequestPayToTPPDTO request);

    /**
     * H5支付
     */
    PaymentParamsVO h5Pay(PaymentRequestPayToTPPDTO request);

    /**
     * 查询支付结果
     */
    WechatPayQueryResult queryPayment(String paymentNo);

    /**
     * 关闭订单
     */
    boolean closePayment(String paymentNo);

    /**
     * 申请退款
     */
    com.aioveu.pay.model.aioveuPayment.RefundResultVO refund(RefundRequestDTO request);
}
