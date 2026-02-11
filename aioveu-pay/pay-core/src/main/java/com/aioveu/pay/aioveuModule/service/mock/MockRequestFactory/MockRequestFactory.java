package com.aioveu.pay.aioveuModule.service.mock.MockRequestFactory;

import com.aioveu.pay.aioveuModule.model.vo.PaymentRequestDTO;
import com.aioveu.pay.aioveuModule.service.AliPay.config.AlipayConfig;
import com.aioveu.pay.aioveuModule.service.mock.config.MockPayConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: MockRequestFactory
 * @Description TODO  模拟支付请求工厂
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/11 20:29
 * @Version 1.0
 **/

@Slf4j
@Component
public class MockRequestFactory {

    /**
     * JSON处理器
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 时间格式化器
     */
    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 默认字符集
     */
    private static final String DEFAULT_CHARSET = "utf-8";

    /**
     * 默认格式
     */
    private static final String DEFAULT_FORMAT = "JSON";

    /**
     * 默认签名类型
     */
    private static final String DEFAULT_SIGN_TYPE = "RSA2";

    /**
     * 默认API版本
     */
    private static final String DEFAULT_VERSION = "1.0";

    /**
     * APP支付方法
     */
    private static final String APP_PAY_METHOD = "alipay.trade.app.pay";

    /**
     * APP支付产品码
     */
    private static final String APP_PRODUCT_CODE = "QUICK_MSECURITY_PAY";

    /**
     * 网页支付方法
     */
    private static final String PAGE_PAY_METHOD = "alipay.trade.page.pay";

    /**
     * 网页支付产品码
     */
    private static final String PAGE_PRODUCT_CODE = "FAST_INSTANT_TRADE_PAY";

    /**
     * 手机网站支付方法
     */
    private static final String WAP_PAY_METHOD = "alipay.trade.wap.pay";

    /**
     * 手机网站支付产品码
     */
    private static final String WAP_PRODUCT_CODE = "QUICK_WAP_WAY";

    /**
     * 查询订单方法
     */
    private static final String QUERY_METHOD = "alipay.trade.query";

    /**
     * 退款方法
     */
    private static final String REFUND_METHOD = "alipay.trade.refund";

    /**
     * 关闭订单方法
     */
    private static final String CLOSE_METHOD = "alipay.trade.close";

    /**
     * 业务内容键
     */
    private static final String BIZ_CONTENT_KEY = "biz_content";

    /**
     * 外部交易号键
     */
    private static final String OUT_TRADE_NO_KEY = "out_trade_no";

    /**
     * 总金额键
     */
    private static final String TOTAL_AMOUNT_KEY = "total_amount";

    /**
     * 订单标题键
     */
    private static final String SUBJECT_KEY = "subject";

    /**
     * 订单描述键
     */
    private static final String BODY_KEY = "body";

    /**
     * 外部请求号键
     */
    private static final String OUT_REQUEST_NO_KEY = "out_request_no";

    /**
     * 退款金额键
     */
    private static final String REFUND_AMOUNT_KEY = "refund_amount";

    /**
     * 退款原因键
     */
    private static final String REFUND_REASON_KEY = "refund_reason";

    /**
     * 支付宝配置
     */
    private final MockPayConfig mockPayConfig;

    /**
     * 构造器注入
     */
    @Autowired
    public MockRequestFactory(
            MockPayConfig mockPayConfig)
    {
        this.mockPayConfig = mockPayConfig;
    }
    /**
     * 创建APP支付请求
     *
     * @param request 支付请求
     * @param mockPayConfig  支付宝配置
     * @return 请求参数
     */
    public Map<String, String> createAppRequest(PaymentRequestDTO request, MockPayConfig mockPayConfig) {
        log.info("创建APP支付请求, 订单号: {}, 金额: {}",
                request.getOrderNo(), request.getAmount());

        Map<String, String> params = new HashMap<>();

        // 设置公共参数
        setCommonParams(params, request, mockPayConfig);

        // APP支付特有参数
        params.put("method", APP_PAY_METHOD);
        params.put("product_code", APP_PRODUCT_CODE);

        log.debug("APP支付请求参数: {}", params);
        return params;
    }

    /**
     * 创建网页支付请求
     *
     * @param request 支付请求
     * @param mockPayConfig  支付宝配置
     * @return 请求参数
     */
    public Map<String, String> createPageRequest(PaymentRequestDTO request, MockPayConfig mockPayConfig) {
        log.info("创建网页支付请求, 订单号: {}, 金额: {}",
                request.getOrderNo(), request.getAmount());

        Map<String, String> params = new HashMap<>();

        // 设置公共参数
        setCommonParams(params, request, mockPayConfig);

        // 网页支付特有参数
        params.put("method", PAGE_PAY_METHOD);
        params.put("product_code", PAGE_PRODUCT_CODE);
//        params.put("return_url", mockPayConfig.getReturnUrl());

        log.debug("网页支付请求参数: {}", params);
        return params;
    }

    /**
     * 创建手机网站支付请求
     *
     * @param request 支付请求
     * @param mockPayConfig  支付宝配置
     * @return 请求参数
     */
    public Map<String, String> createWapRequest(PaymentRequestDTO request, MockPayConfig mockPayConfig) {
        log.info("创建手机网站支付请求, 订单号: {}, 金额: {}",
                request.getOrderNo(), request.getAmount());

        Map<String, String> params = new HashMap<>();

        // 设置公共参数
        setCommonParams(params, request, mockPayConfig);

        // 手机网站支付特有参数
        params.put("method", WAP_PAY_METHOD);
        params.put("product_code", WAP_PRODUCT_CODE);
//        params.put("return_url", mockPayConfig.getReturnUrl());

        log.debug("手机网站支付请求参数: {}", params);
        return params;
    }

    /**
     * 创建查询订单请求
     *
     * @param paymentNo 支付订单号
     * @param mockPayConfig    支付宝配置
     * @return 请求参数
     */
    public Map<String, String> createQueryRequest(String paymentNo, MockPayConfig mockPayConfig) {
        log.info("创建查询订单请求, 订单号: {}", paymentNo);

        Map<String, String> params = new HashMap<>();

        // 设置公共参数
        setCommonParams(params, null, mockPayConfig);

        // 查询特有参数
        params.put("method", QUERY_METHOD);

        // 构建业务参数
        Map<String, Object> bizContent = new HashMap<>();
        bizContent.put(OUT_TRADE_NO_KEY, paymentNo);
        params.put(BIZ_CONTENT_KEY, toJson(bizContent));

        log.debug("查询订单请求参数: {}", params);
        return params;
    }

    /**
     * 创建退款请求
     *
     * @param refundNo      退款单号
     * @param paymentNo     支付订单号
     * @param refundAmount  退款金额
     * @param refundReason  退款原因
     * @param mockPayConfig        模拟支付配置
     * @return 请求参数
     */
    public Map<String, String> createRefundRequest(String refundNo, String paymentNo,
                                                   BigDecimal refundAmount, String refundReason,
                                                   MockPayConfig mockPayConfig) {
        log.info("创建退款请求, 退款单号: {}, 支付订单号: {}, 退款金额: {}",
                refundNo, paymentNo, refundAmount);

        Map<String, String> params = new HashMap<>();

        // 设置公共参数
        setCommonParams(params, null, mockPayConfig);

        // 退款特有参数
        params.put("method", REFUND_METHOD);

        // 构建业务参数
        Map<String, Object> bizContent = new HashMap<>();
        bizContent.put(OUT_TRADE_NO_KEY, paymentNo);
        bizContent.put(OUT_REQUEST_NO_KEY, refundNo);
        bizContent.put(REFUND_AMOUNT_KEY, refundAmount.toString());

        if (refundReason != null && !refundReason.trim().isEmpty()) {
            bizContent.put(REFUND_REASON_KEY, refundReason);
        }

        params.put(BIZ_CONTENT_KEY, toJson(bizContent));

        log.debug("退款请求参数: {}", params);
        return params;
    }

    /**
     * 创建关闭订单请求
     *
     * @param paymentNo 支付订单号
     * @param mockPayConfig   模拟支付配置
     * @return 请求参数
     */
    public Map<String, String> createCloseRequest(String paymentNo, MockPayConfig mockPayConfig) {
        log.info("创建关闭订单请求, 订单号: {}", paymentNo);

        Map<String, String> params = new HashMap<>();

        // 设置公共参数
        setCommonParams(params, null, mockPayConfig);

        // 关闭订单特有参数
        params.put("method", CLOSE_METHOD);

        // 构建业务参数
        Map<String, Object> bizContent = new HashMap<>();
        bizContent.put(OUT_TRADE_NO_KEY, paymentNo);
        params.put(BIZ_CONTENT_KEY, toJson(bizContent));

        log.debug("关闭订单请求参数: {}", params);
        return params;
    }

    /**
     * 设置公共参数
     *
     * @param params  参数Map
     * @param request 支付请求
     * @param mockPayConfig  模拟支付配置配置
     */
    private void setCommonParams(Map<String, String> params, PaymentRequestDTO request,
                                 MockPayConfig mockPayConfig) {
        // 系统参数
        params.put("app_id", mockPayConfig.getWechat().getAppId());
        params.put("format", DEFAULT_FORMAT);
        params.put("charset", DEFAULT_CHARSET);
        params.put("sign_type", DEFAULT_SIGN_TYPE);
        params.put("version", DEFAULT_VERSION);
        params.put("timestamp", getCurrentTimestamp());

        // 异步通知URL
        if (mockPayConfig.getWechat().getNotifyUrl() != null) {
            params.put("notify_url", mockPayConfig.getWechat().getNotifyUrl());
        }

        // 业务参数 - 只有在支付请求时才设置
        if (request != null) {
            Map<String, Object> bizContent = new HashMap<>();
            bizContent.put(OUT_TRADE_NO_KEY, request.getOrderNo());
            bizContent.put(TOTAL_AMOUNT_KEY, request.getAmount().toString());
            bizContent.put(SUBJECT_KEY, request.getSubject());

            if (request.getBody() != null && !request.getBody().trim().isEmpty()) {
                bizContent.put(BODY_KEY, request.getBody());
            }

            params.put(BIZ_CONTENT_KEY, toJson(bizContent));
        }
    }

    /**
     * 创建EasySDK支付客户端
     *
     * @return APP支付客户端
     */
    public com.alipay.easysdk.payment.app.Client createEasyAppClient() {
        try {

            //修复了 EasySDK 方法调用
            return com.alipay.easysdk.factory.Factory.Payment.App();
        } catch (Exception e) {
            log.error("创建支付宝APP支付客户端失败", e);
            throw new RuntimeException("创建支付宝APP支付客户端失败", e);
        }
    }

    /**
     * 创建EasySDK网页支付客户端
     *
     * @return 网页支付客户端
     */
    public com.alipay.easysdk.payment.page.Client createEasyPageClient() {
        try {
            return com.alipay.easysdk.factory.Factory.Payment.Page();
        } catch (Exception e) {
            log.error("创建支付宝网页支付客户端失败", e);
            throw new RuntimeException("创建支付宝网页支付客户端失败", e);
        }
    }

    /**
     * 创建EasySDK手机网站支付客户端
     *
     * @return 手机网站支付客户端
     */
    public com.alipay.easysdk.payment.wap.Client createEasyWapClient() {
        try {
            return com.alipay.easysdk.factory.Factory.Payment.Wap();
        } catch (Exception e) {
            log.error("创建支付宝手机网站支付客户端失败", e);
            throw new RuntimeException("创建支付宝手机网站支付客户端失败", e);
        }
    }

    /**
     * 创建EasySDK通用支付客户端
     *
     * @return 通用支付客户端
     */
    public com.alipay.easysdk.payment.common.Client createEasyCommonClient() {
        try {
            return com.alipay.easysdk.factory.Factory.Payment.Common();
        } catch (Exception e) {
            log.error("创建支付宝通用支付客户端失败", e);
            throw new RuntimeException("创建支付宝通用支付客户端失败", e);
        }
    }

    /**
     * 创建统一订单模型（用于EasySDK）
     *
     * @param request     支付请求
     * @param productCode 产品码
     * @return 订单模型JSON
     */
    public String createEasyOrderModel(PaymentRequestDTO request, String productCode) {
        Map<String, Object> order = new HashMap<>();
        order.put(SUBJECT_KEY, request.getSubject());
        order.put(OUT_TRADE_NO_KEY, request.getOrderNo());
        order.put(TOTAL_AMOUNT_KEY, request.getAmount().toString());
        order.put("product_code", productCode);

        if (request.getBody() != null && !request.getBody().trim().isEmpty()) {
            order.put(BODY_KEY, request.getBody());
        }

        return toJson(order);
    }

    /**
     * 获取当前时间戳
     *
     * @return 当前时间戳字符串
     */
    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(TIMESTAMP_FORMATTER);
    }

    /**
     * 对象转JSON
     *
     * @param obj 对象
     * @return JSON字符串
     */
    private String toJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("对象转JSON失败", e);
            throw new RuntimeException("对象转JSON失败", e);
        }
    }

    /**
     * 验证支付请求参数
     *
     * @param request 支付请求
     */
    public void validatePaymentRequest(PaymentRequestDTO request) {
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
}
