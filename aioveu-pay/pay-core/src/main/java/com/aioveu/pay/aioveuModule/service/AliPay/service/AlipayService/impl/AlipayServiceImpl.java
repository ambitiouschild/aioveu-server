package com.aioveu.pay.aioveuModule.service.AliPay.service.AlipayService.impl;

import cn.hutool.core.date.DateUtil;
import com.aioveu.pay.aioveuModule.enums.PaymentStatusEnum;
import com.aioveu.pay.aioveuModule.enums.RefundStatusEnum;
import com.aioveu.pay.aioveuModule.model.vo.*;
import com.aioveu.pay.aioveuModule.service.AliPay.AlipayRequestFactory.AlipayRequestFactory;
import com.aioveu.pay.aioveuModule.service.AliPay.config.AlipayConfig;
import com.aioveu.pay.aioveuModule.service.AliPay.service.AlipayService.AlipayService;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

/**
 * @ClassName: AlipayStrategyImpl
 * @Description TODO 支付宝支付服务实现 - 使用工厂模式
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 17:50
 * @Version 1.0
 **/

@Service
@Slf4j
public class AlipayServiceImpl implements AlipayService {

    private final AlipayRequestFactory requestFactory;
    private final AlipayConfig alipayConfig;

    @Autowired
    public AlipayServiceImpl(AlipayRequestFactory requestFactory, AlipayConfig alipayConfig) {
        this.requestFactory = requestFactory;
        this.alipayConfig = alipayConfig;
    }

    /**
     * APP支付
     */
    @Override
    public PaymentParamsVO appPay(PaymentRequestDTO request) {
        Map<String, String> params = requestFactory.createAppRequest(request, alipayConfig);

        // 使用EasySDK
        String orderString = requestFactory.createEasyAppClient()
                .pay(request.getSubject(), request.getOrderNo(), request.getAmount().toString());

        return PaymentParamsVO.builder()
                .paymentNo(request.getOrderNo())
                .paymentParams(orderString)
                .build();
    }

    /**
     * 网页支付
     */
    @Override
    public PaymentParamsVO pagePay(PaymentRequestDTO request)  {
        Map<String, String> params = requestFactory.createPageRequest(request, alipayConfig);

        // 使用EasySDK
        String form = requestFactory.createEasyPageClient()
                .pay(request.getSubject(), request.getOrderNo(), request.getAmount().toString(),
                        alipayConfig.getReturnUrl());

        return PaymentParamsVO.builder()
                .paymentNo(request.getOrderNo())
                .paymentParams(form)
                .build();
    }

    /**
     * 手机网站支付
     */
    @Override
    public PaymentParamsVO wapPay(PaymentRequestDTO request)  {
        Map<String, String> params = requestFactory.createWapRequest(request, alipayConfig);

        // 使用EasySDK
        String form = requestFactory.createEasyWapClient()
                .pay(request.getSubject(), request.getOrderNo(), request.getAmount().toString(),
                        "", alipayConfig.getReturnUrl());

        return PaymentParamsVO.builder()
                .paymentNo(request.getOrderNo())
                .paymentParams(form)
                .build();
    }

    /**
     * 查询订单状态
     */
    @Override
    public PaymentStatusVO queryPayment(String paymentNo)  {
        Map<String, String> params = requestFactory.createQueryRequest(paymentNo, alipayConfig);

        // 使用EasySDK
        com.alipay.easysdk.payment.common.models.AlipayTradeQueryResponse response =
                requestFactory.createEasyCommonClient().query(paymentNo);

        return convertToPaymentStatus(response);
    }

    /**
     * 退款
     */
    @Override
    public RefundResultVO refund(RefundRequestDTO request)  {
        Map<String, String> params = requestFactory.createRefundRequest(
                request.getRefundNo(), request.getPaymentNo(),
                request.getRefundAmount(), request.getRefundReason(), alipayConfig);

        // 使用EasySDK
        com.alipay.easysdk.payment.common.models.AlipayTradeRefundResponse response =
                requestFactory.createEasyCommonClient()
                        .refund(request.getPaymentNo(), request.getRefundAmount().toString());

        return convertToRefundResult(request, response);
    }

    /**
     * 关闭订单
     */
    @Override
    public boolean closePayment(String paymentNo)  {
        Map<String, String> params = requestFactory.createCloseRequest(paymentNo, alipayConfig);

        // 使用EasySDK
        com.alipay.easysdk.payment.common.models.AlipayTradeCloseResponse response =
                requestFactory.createEasyCommonClient().close(paymentNo);

        return "10000".equals(response.code);
    }

    /**
     * 转换支付状态
     */
    private PaymentStatusVO convertToPaymentStatus(
            com.alipay.easysdk.payment.common.models.AlipayTradeQueryResponse response) {

        if (!"10000".equals(response.code)) {
            throw new RuntimeException("查询失败: " + response.subMsg);
        }

        return PaymentStatusVO.builder()
                .paymentNo(response.outTradeNo)
                .thirdPaymentNo(response.tradeNo)
                .amount(new BigDecimal(response.totalAmount))
                .paymentStatus(convertAlipayStatus(response.tradeStatus))
                .paymentTime(response.sendPayDate != null ?
                        java.util.Date.from(java.time.Instant.parse(response.sendPayDate)) : null)
                .build();
    }

    /**
     * 转换退款结果
     */
    private RefundResultVO convertToRefundResult(RefundRequestDTO request,
                                                 com.alipay.easysdk.payment.common.models.AlipayTradeRefundResponse response) {

        if (!"10000".equals(response.code)) {
            throw new RuntimeException("退款失败: " + response.subMsg);
        }

        return RefundResultVO.builder()
                .refundNo(request.getRefundNo())
                .thirdRefundNo(response.tradeNo)
                .refundAmount(new BigDecimal(response.refundFee))
                .refundStatus(RefundStatusEnum.SUCCESS.getValue())
                .refundTime(new java.util.Date())
                .build();
    }

    /**
     * 转换支付宝状态
     */
    private Integer convertAlipayStatus(String tradeStatus) {
        if (tradeStatus == null) {
            return PaymentStatusEnum.FAILED.getValue();
        }

        switch (tradeStatus) {
            case "TRADE_SUCCESS":
            case "TRADE_FINISHED":
                return PaymentStatusEnum.SUCCESS.getValue();
            case "WAIT_BUYER_PAY":
                return PaymentStatusEnum.PENDING.getValue();
            case "TRADE_CLOSED":
                return PaymentStatusEnum.CLOSED.getValue();
            default:
                return PaymentStatusEnum.FAILED.getValue();
        }
    }

    /**
     * 获取过期时间
     */
    private String getExpireTime(Integer expireMinutes) {
        if (expireMinutes == null || expireMinutes <= 0) {
            expireMinutes = 30; // 默认30分钟
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expireMinutes);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(calendar.getTime());
    }
}
