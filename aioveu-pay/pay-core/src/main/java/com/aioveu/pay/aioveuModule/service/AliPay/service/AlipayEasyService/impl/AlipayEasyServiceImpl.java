package com.aioveu.pay.aioveuModule.service.AliPay.service.AlipayEasyService.impl;

import com.aioveu.pay.aioveuModule.enums.PaymentStatusEnum;
import com.aioveu.pay.aioveuModule.enums.RefundStatusEnum;
import com.aioveu.pay.aioveuModule.model.vo.*;
import com.aioveu.pay.aioveuModule.service.AliPay.AlipayRequestFactory.AlipayRequestFactory;
import com.aioveu.pay.aioveuModule.service.AliPay.config.AlipayConfig;
import com.aioveu.pay.aioveuModule.service.AliPay.service.AlipayEasyService.AlipayEasyService;
import com.aioveu.pay.aioveuModule.service.AliPay.service.AlipayService.AlipayService;
import com.aioveu.pay.aioveuModule.service.WechatPay.utils.aliPay.aioveuAlipayGeneratePayParamsUtil;
import com.alipay.easysdk.payment.app.models.AlipayTradeAppPayResponse;
import com.alipay.easysdk.payment.common.models.AlipayTradeCloseResponse;
import com.alipay.easysdk.payment.common.models.AlipayTradeQueryResponse;
import com.alipay.easysdk.payment.common.models.AlipayTradeRefundResponse;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.alipay.easysdk.payment.wap.models.AlipayTradeWapPayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @ClassName: AlipayEasyServiceImpl
 * @Description TODO 支付宝EasySDK支付服务实现   - 返回支付参数
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 21:02
 * @Version 1.0
 **/

@Service
@Slf4j
public class AlipayEasyServiceImpl implements AlipayEasyService {

    /**
     * 支付宝请求工厂
     */
    private final AlipayRequestFactory requestFactory;

    /**
     * 支付宝配置
     */
    private final AlipayConfig alipayConfig;

    /**
     * 日期格式化器
     */
    private static final SimpleDateFormat DATE_FORMATTER =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 支付过期时间（分钟）
     */
    private static final int DEFAULT_EXPIRE_MINUTES = 30;

    /**
     * 支付宝成功响应码
     */
    private static final String ALIPAY_SUCCESS_CODE = "10000";

    /**
     * 支付宝成功状态
     */
    private static final String TRADE_SUCCESS = "TRADE_SUCCESS";

    /**
     * 支付宝交易完成状态
     */
    private static final String TRADE_FINISHED = "TRADE_FINISHED";

    /**
     * 支付宝等待支付状态
     */
    private static final String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";

    /**
     * 支付宝交易关闭状态
     */
    private static final String TRADE_CLOSED = "TRADE_CLOSED";

    aioveuAlipayGeneratePayParamsUtil aioveuAlipayGeneratePayParamsUtil;


    /**
     * 构造器注入
     */
    @Autowired
    public AlipayEasyServiceImpl(AlipayRequestFactory requestFactory, AlipayConfig alipayConfig) {
        this.requestFactory = requestFactory;
        this.alipayConfig = alipayConfig;
    }

    /**
     * APP支付
     *
     * @param request 支付请求
     * @return 支付参数
     */
    @Override
    public PaymentParamsVO appPay(PaymentRequestDTO request) {
        try {
            log.info("支付宝APP支付, 订单号: {}, 金额: {}",
                    request.getOrderNo(), request.getAmount());

            // 验证请求参数
            validatePaymentRequest(request);

            // 创建支付请求
            Map<String, String> params = requestFactory.createAppRequest(request, alipayConfig);
            log.debug("支付宝APP支付请求参数: {}", params);

            // 使用EasySDK进行支付
            AlipayTradeAppPayResponse response  = requestFactory.createEasyAppClient()
                    .pay(request.getSubject(), request.getOrderNo(), request.getAmount().toString());

//            if (!ALIPAY_SUCCESS_CODE.equals(response.code)) {
//                throw new RuntimeException(String.format("支付宝APP支付失败: %s - %s",
//                        response.subCode, response.subMsg));
//            }
            // 生成支付参数
            Map<String, Object> payParams = aioveuAlipayGeneratePayParamsUtil.generateAppPayParams(response.getBody());

            PaymentParamsVO result = PaymentParamsVO.builder()
                    .paymentNo(request.getOrderNo())
                    .orderNo(request.getOrderNo())
                    .amount(request.getAmount())
                    .subject(request.getSubject())
                    .body(request.getBody())
                    .payType("JSAPI")
                    .channel("WECHAT")
                    .prepayId("111")
                    .payParams(payParams)
                    .createTime(System.currentTimeMillis())
                    .expireTime(System.currentTimeMillis() + 30 * 60 * 1000) // 30分钟
                    .build();

            log.info("支付宝APP支付成功, 订单号: {}", request.getOrderNo());
            return result;

        } catch (Exception e) {
            log.error("支付宝APP支付失败, 订单号: {}", request.getOrderNo(), e);
            throw new RuntimeException("支付宝APP支付失败: " + e.getMessage(), e);
        }
    }

    /**
     * 网页支付
     *
     * @param request 支付请求
     * @return 支付参数
     */
    @Override
    public PaymentParamsVO pagePay(PaymentRequestDTO request) {
        try {
            log.info("支付宝网页支付, 订单号: {}, 金额: {}",
                    request.getOrderNo(), request.getAmount());

            // 验证请求参数
            validatePaymentRequest(request);

            // 创建支付请求
            Map<String, String> params = requestFactory.createPageRequest(request, alipayConfig);
            log.debug("支付宝网页支付请求参数: {}", params);

            // 使用EasySDK进行支付
            AlipayTradePagePayResponse response  = requestFactory.createEasyPageClient()
                    .pay(request.getSubject(), request.getOrderNo(),
                            request.getAmount().toString(), alipayConfig.getReturnUrl());


//            if (!ALIPAY_SUCCESS_CODE.equals(response.code)) {
//                throw new RuntimeException(String.format("支付宝网页支付失败: %s - %s",
//                        response.subCode, response.subMsg));
//            }
            // 生成支付参数
            Map<String, Object> payParams = aioveuAlipayGeneratePayParamsUtil.generateAppPayParams(response.getBody());

            PaymentParamsVO result = PaymentParamsVO.builder()
                    .paymentNo(request.getOrderNo())
                    .orderNo(request.getOrderNo())
                    .amount(request.getAmount())
                    .subject(request.getSubject())
                    .body(request.getBody())
                    .payType("JSAPI")
                    .channel("WECHAT")
                    .prepayId("111")
                    .payParams(payParams)
                    .createTime(System.currentTimeMillis())
                    .expireTime(System.currentTimeMillis() + 30 * 60 * 1000) // 30分钟
                    .build();

            log.info("支付宝网页支付成功, 订单号: {}", request.getOrderNo());
            return result;

        } catch (Exception e) {
            log.error("支付宝网页支付失败, 订单号: {}", request.getOrderNo(), e);
            throw new RuntimeException("支付宝网页支付失败: " + e.getMessage(), e);
        }
    }

    /**
     * 手机网站支付
     *
     * @param request 支付请求
     * @return 支付参数
     */
    @Override
    public PaymentParamsVO wapPay(PaymentRequestDTO request) {
        try {
            log.info("支付宝手机网站支付, 订单号: {}, 金额: {}",
                    request.getOrderNo(), request.getAmount());

            // 验证请求参数
            validatePaymentRequest(request);

            // 创建支付请求
            Map<String, String> params = requestFactory.createWapRequest(request, alipayConfig);
            log.debug("支付宝手机网站支付请求参数: {}", params);

            // 使用EasySDK进行支付
            AlipayTradeWapPayResponse response = requestFactory.createEasyWapClient()
                    .pay(request.getSubject(), request.getOrderNo(),
                            request.getAmount().toString(), "", alipayConfig.getReturnUrl());

//            if (!ALIPAY_SUCCESS_CODE.equals(response.code)) {
//                throw new RuntimeException(String.format("支付宝手机网站支付失败: %s - %s",
//                        response.subCode, response.subMsg));
//            }
            // 生成支付参数
            Map<String, Object> payParams = aioveuAlipayGeneratePayParamsUtil.generateAppPayParams(response.getBody());

            PaymentParamsVO result = PaymentParamsVO.builder()
                    .paymentNo(request.getOrderNo())
                    .orderNo(request.getOrderNo())
                    .amount(request.getAmount())
                    .subject(request.getSubject())
                    .body(request.getBody())
                    .payType("JSAPI")
                    .channel("WECHAT")
                    .prepayId("111")
                    .payParams(payParams)
                    .createTime(System.currentTimeMillis())
                    .expireTime(System.currentTimeMillis() + 30 * 60 * 1000) // 30分钟
                    .build();

            log.info("支付宝手机网站支付成功, 订单号: {}", request.getOrderNo());
            return result;

        } catch (Exception e) {
            log.error("支付宝手机网站支付失败, 订单号: {}", request.getOrderNo(), e);
            throw new RuntimeException("支付宝手机网站支付失败: " + e.getMessage(), e);
        }
    }

    /**
     * 查询订单状态
     *
     * @param paymentNo 支付订单号
     * @return 支付状态
     */
    @Override
    public PaymentStatusVO queryPayment(String paymentNo) {
        try {
            log.info("查询支付宝订单状态, 订单号: {}", paymentNo);

            if (paymentNo == null || paymentNo.trim().isEmpty()) {
                throw new IllegalArgumentException("支付订单号不能为空");
            }

            // 创建查询请求
            Map<String, String> params = requestFactory.createQueryRequest(paymentNo, alipayConfig);
            log.debug("支付宝查询订单请求参数: {}", params);

            // 使用EasySDK查询订单
            AlipayTradeQueryResponse response = requestFactory.createEasyCommonClient()
                    .query(paymentNo);

            PaymentStatusVO result = convertToPaymentStatus(response);
            log.info("查询支付宝订单状态成功, 订单号: {}, 状态: {}",
                    paymentNo, result.getPaymentStatus());

            return result;

        } catch (Exception e) {
            log.error("查询支付宝订单状态失败, 订单号: {}", paymentNo, e);
            throw new RuntimeException("查询支付宝订单状态失败: " + e.getMessage(), e);
        }
    }

    /**
     * 退款
     *
     * @param request 退款请求
     * @return 退款结果
     */
    @Override
    public RefundResultVO refund(RefundRequestDTO request) {
        try {
            log.info("支付宝退款, 退款单号: {}, 支付订单号: {}, 退款金额: {}",
                    request.getRefundNo(), request.getPaymentNo(), request.getRefundAmount());

            // 验证退款请求
            validateRefundRequest(request);

            // 创建退款请求
            Map<String, String> params = requestFactory.createRefundRequest(
                    request.getRefundNo(), request.getPaymentNo(),
                    request.getRefundAmount(), request.getRefundReason(), alipayConfig);
            log.debug("支付宝退款请求参数: {}", params);

            // 使用EasySDK进行退款
            AlipayTradeRefundResponse response = requestFactory.createEasyCommonClient()
                    .refund(request.getPaymentNo(), request.getRefundAmount().toString());

            RefundResultVO result = convertToRefundResult(request, response);
            log.info("支付宝退款成功, 退款单号: {}", request.getRefundNo());

            return result;

        } catch (Exception e) {
            log.error("支付宝退款失败, 退款单号: {}", request.getRefundNo(), e);
            throw new RuntimeException("支付宝退款失败: " + e.getMessage(), e);
        }
    }

    /**
     * 关闭订单
     *
     * @param paymentNo 支付订单号
     * @return 是否关闭成功
     */
    @Override
    public boolean closePayment(String paymentNo) {
        try {
            log.info("关闭支付宝订单, 订单号: {}", paymentNo);

            if (paymentNo == null || paymentNo.trim().isEmpty()) {
                throw new IllegalArgumentException("支付订单号不能为空");
            }

            // 创建关闭请求
            Map<String, String> params = requestFactory.createCloseRequest(paymentNo, alipayConfig);
            log.debug("支付宝关闭订单请求参数: {}", params);

            // 使用EasySDK关闭订单
            AlipayTradeCloseResponse response = requestFactory.createEasyCommonClient()
                    .close(paymentNo);

            boolean success = ALIPAY_SUCCESS_CODE.equals(response.code);
            log.info("关闭支付宝订单结果, 订单号: {}, 成功: {}", paymentNo, success);

            return success;

        } catch (Exception e) {
            log.error("关闭支付宝订单失败, 订单号: {}", paymentNo, e);
            throw new RuntimeException("关闭支付宝订单失败: " + e.getMessage(), e);
        }
    }

    /**
     * 转换支付状态
     *
     * @param response 支付宝查询响应
     * @return 支付状态VO
     */
    private PaymentStatusVO convertToPaymentStatus(AlipayTradeQueryResponse response) {
        if (!ALIPAY_SUCCESS_CODE.equals(response.code)) {
            String errorMsg = String.format("查询失败: %s - %s", response.subCode, response.subMsg);
            throw new RuntimeException(errorMsg);
        }

        return PaymentStatusVO.builder()
                .paymentNo(response.outTradeNo)
                .thirdPaymentNo(response.tradeNo)
                .amount(parseBigDecimal(response.totalAmount))
                .paymentStatus(convertAlipayStatus(response.tradeStatus))
                .paymentTime(parsePaymentTime(response.sendPayDate))
                .build();
    }

    /**
     * 转换退款结果
     *
     * @param request  退款请求
     * @param response 支付宝退款响应
     * @return 退款结果VO
     */
    private RefundResultVO convertToRefundResult(RefundRequestDTO request,
                                                 AlipayTradeRefundResponse response) {
        if (!ALIPAY_SUCCESS_CODE.equals(response.code)) {
            String errorMsg = String.format("退款失败: %s - %s", response.subCode, response.subMsg);
            throw new RuntimeException(errorMsg);
        }

        return RefundResultVO.builder()
                .refundNo(request.getRefundNo())
                .thirdRefundNo(response.tradeNo)
                .refundAmount(parseBigDecimal(response.refundFee))
                .refundStatus(RefundStatusEnum.SUCCESS.getValue())
                .refundTime(new Date())
                .build();
    }

    /**
     * 转换支付宝状态
     *
     * @param tradeStatus 支付宝交易状态
     * @return 支付状态枚举值
     */
    private Integer convertAlipayStatus(String tradeStatus) {
        if (tradeStatus == null) {
            return PaymentStatusEnum.FAILED.getValue();
        }

        switch (tradeStatus) {
            case TRADE_SUCCESS:
            case TRADE_FINISHED:
                return PaymentStatusEnum.SUCCESS.getValue();
            case WAIT_BUYER_PAY:
                return PaymentStatusEnum.PENDING.getValue();
            case TRADE_CLOSED:
                return PaymentStatusEnum.CLOSED.getValue();
            default:
                return PaymentStatusEnum.FAILED.getValue();
        }
    }

    /**
     * 获取过期时间
     *
     * @param expireMinutes 过期分钟数
     * @return 过期时间字符串
     */
    private String getExpireTime(Integer expireMinutes) {
        int minutes = (expireMinutes == null || expireMinutes <= 0)
                ? DEFAULT_EXPIRE_MINUTES : expireMinutes;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minutes);

        synchronized (DATE_FORMATTER) {
            return DATE_FORMATTER.format(calendar.getTime());
        }
    }

    /**
     * 验证支付请求参数
     *
     * @param request 支付请求
     */
    private void validatePaymentRequest(PaymentRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("支付请求不能为空");
        }

        if (request.getOrderNo() == null || request.getOrderNo().trim().isEmpty()) {
            throw new IllegalArgumentException("订单号不能为空");
        }

        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("支付金额必须大于0");
        }

        if (request.getSubject() == null || request.getSubject().trim().isEmpty()) {
            throw new IllegalArgumentException("订单标题不能为空");
        }
    }

    /**
     * 验证退款请求参数
     *
     * @param request 退款请求
     */
    private void validateRefundRequest(RefundRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("退款请求不能为空");
        }

        if (request.getRefundNo() == null || request.getRefundNo().trim().isEmpty()) {
            throw new IllegalArgumentException("退款单号不能为空");
        }

        if (request.getPaymentNo() == null || request.getPaymentNo().trim().isEmpty()) {
            throw new IllegalArgumentException("支付订单号不能为空");
        }

        if (request.getRefundAmount() == null ||
                request.getRefundAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("退款金额必须大于0");
        }
    }

    /**
     * 解析金额
     *
     * @param amountStr 金额字符串
     * @return 金额对象
     */
    private BigDecimal parseBigDecimal(String amountStr) {
        if (amountStr == null || amountStr.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }

        try {
            return new BigDecimal(amountStr);
        } catch (NumberFormatException e) {
            log.error("解析金额失败: {}", amountStr, e);
            return BigDecimal.ZERO;
        }
    }

    /**
     * 解析支付时间
     *
     * @param timeStr 时间字符串
     * @return 日期对象
     */
    private Date parsePaymentTime(String timeStr) {
        if (timeStr == null || timeStr.trim().isEmpty()) {
            return null;
        }

        try {
            // 尝试解析ISO格式
            Instant instant = Instant.parse(timeStr);
            return Date.from(instant);
        } catch (DateTimeParseException e) {
            log.warn("解析支付时间失败: {}, 使用当前时间", timeStr, e);
            return new Date();
        }
    }
}
