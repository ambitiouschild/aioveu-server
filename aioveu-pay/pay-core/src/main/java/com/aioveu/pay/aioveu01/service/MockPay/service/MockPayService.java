package com.aioveu.pay.aioveu01.service.MockPay.service;

import com.aioveu.pay.model.aioveuPayAdapter.MockPayQueryResult;
import com.aioveu.pay.model.aioveuPayment.PaymentParamsVO;
import com.aioveu.pay.model.aioveuPayment.RefundRequestDTO;
import com.aioveu.pay.model.aioveuPayment.request.PaymentRequestPayToTPPDTO;

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
    PaymentParamsVO jsapiPay(PaymentRequestPayToTPPDTO request);

    /**
     * 模拟支付宝支付
     */
    PaymentParamsVO appPay(PaymentRequestPayToTPPDTO request);

    /**
     * 模拟余额支付
     */
    PaymentParamsVO h5Pay(PaymentRequestPayToTPPDTO request);

    /**
     * 查询订单状态
     */
    MockPayQueryResult queryPayment(String paymentNo);

    /**
     * 退款
     */
    com.aioveu.pay.model.aioveuPayment.RefundResultVO refund(RefundRequestDTO request);

    /**
     * 关闭订单
     */
    boolean closePayment(String paymentNo);

}
