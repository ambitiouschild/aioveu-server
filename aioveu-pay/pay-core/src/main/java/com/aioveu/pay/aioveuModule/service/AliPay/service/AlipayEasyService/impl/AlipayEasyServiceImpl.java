package com.aioveu.pay.aioveuModule.service.AliPay.service.AlipayEasyService.impl;

import com.aioveu.pay.aioveuModule.model.vo.PaymentParamsVO;
import com.aioveu.pay.aioveuModule.model.vo.PaymentRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: AlipayEasyServiceImpl
 * @Description TODO
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 21:02
 * @Version 1.0
 **/

@Service
@Slf4j
public class AlipayEasyServiceImpl {

    @Autowired
    private com.alipay.easysdk.factory.Factory factory;

    /**
     * 使用EasySDK进行支付
     */
    public PaymentParamsVO easyPay(PaymentRequestDTO request) throws Exception {
        // 1. APP支付
        com.alipay.easysdk.payment.app.Client appClient = factory.app();
        String orderString = appClient.pay(
                request.getSubject(),
                request.getOrderNo(),
                request.getAmount().toString()
        );

        // 2. 网站支付
        com.alipay.easysdk.payment.page.Client pageClient = factory.page();
        String pagePayHtml = pageClient.pay(
                request.getSubject(),
                request.getOrderNo(),
                request.getAmount().toString(),
                request.getReturnUrl()
        );

        // 3. 手机网站支付
        com.alipay.easysdk.payment.wap.Client wapClient = factory.wap();
        String wapPayHtml = wapClient.pay(
                request.getSubject(),
                request.getOrderNo(),
                request.getAmount().toString(),
                "",
                request.getReturnUrl()
        );

        return PaymentParamsVO.builder()
                .paymentParams(orderString)
                .build();
    }

    /**
     * 使用EasySDK查询订单
     */
    public PaymentStatusVO easyQuery(String paymentNo) throws Exception {
        com.alipay.easysdk.payment.common.Client commonClient = factory.common();
        com.alipay.easysdk.payment.common.models.AlipayTradeQueryResponse response =
                commonClient.query(paymentNo);

        if ("10000".equals(response.code)) {
            PaymentStatusVO statusVO = new PaymentStatusVO();
            statusVO.setPaymentNo(paymentNo);
            statusVO.setThirdPaymentNo(response.getTradeNo());
            statusVO.setAmount(new BigDecimal(response.getTotalAmount()));
            statusVO.setPaymentStatus(convertAlipayStatus(response.getTradeStatus()));

            if (response.getSendPayDate() != null) {
                statusVO.setPaymentTime(DateUtil.parse(response.getSendPayDate()));
            }

            return statusVO;
        } else {
            throw new BusinessException("查询失败: " + response.getSubMsg());
        }
    }

    /**
     * 使用EasySDK退款
     */
    public RefundResultVO easyRefund(RefundRequestDTO request) throws Exception {
        com.alipay.easysdk.payment.common.Client commonClient = factory.common();

        com.alipay.easysdk.payment.common.models.AlipayTradeRefundResponse response =
                commonClient.refund(
                        request.getPaymentNo(),
                        request.getRefundAmount().toString()
                );

        if ("10000".equals(response.code)) {
            RefundResultVO result = new RefundResultVO();
            result.setRefundNo(request.getRefundNo());
            result.setThirdRefundNo(response.getTradeNo());
            result.setRefundAmount(new BigDecimal(response.getRefundFee()));
            result.setRefundStatus(RefundStatus.SUCCESS.getCode());
            result.setRefundTime(new Date());

            return result;
        } else {
            throw new BusinessException("退款失败: " + response.getSubMsg());
        }
    }
}
