package com.aioveu.pay.aioveu01.service.AliPay.service.AlipayEasyService;

import com.aioveu.pay.model.aioveuPayment.PaymentParamsVO;
import com.aioveu.pay.model.aioveuPayment.PaymentStatusVO;
import com.aioveu.pay.model.aioveuPayment.RefundRequestDTO;
import com.aioveu.pay.model.aioveuPayment.request.PaymentRequestPayToTPPDTO;

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
    PaymentParamsVO appPay(PaymentRequestPayToTPPDTO request);


    /**
     * 网页支付
     */
    PaymentParamsVO pagePay(PaymentRequestPayToTPPDTO request);

    /**
     * 手机网站支付
     */
    PaymentParamsVO wapPay(PaymentRequestPayToTPPDTO request);

    /**
     * 查询订单状态
     */
    PaymentStatusVO queryPayment(String paymentNo);

    /**
     * 退款
     */
    com.aioveu.pay.model.aioveuPayment.RefundResultVO refund(RefundRequestDTO request);

    /**
     * 关闭订单
     */
    boolean closePayment(String paymentNo);
}
