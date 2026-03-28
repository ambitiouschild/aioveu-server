package com.aioveu.pay.aioveu01.service.AliPay.service.AlipayService.impl;

import com.aioveu.pay.aioveu01.service.WechatPay.utils.aliPay.aioveuAlipayGeneratePayParamsUtil;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.aioveu.pay.aioveu01.enums.PaymentStatusEnum;
import com.aioveu.pay.aioveu01.enums.RefundStatusEnum;
import com.aioveu.pay.aioveu01.model.vo.*;
import com.aioveu.pay.aioveu01.service.AliPay.AlipayRequestFactory.AlipayRequestFactory;
import com.aioveu.pay.aioveu01.service.AliPay.config.AlipayConfig;
import com.aioveu.pay.aioveu01.service.AliPay.service.AlipayService.AlipayService;
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
 * @ClassName: AlipayStrategyImpl
 * @Description TODO 传统的 Alipay API，不是 EasySDK,支付宝支付服务实现 - 使用工厂模式
 *                    - 返回支付参数
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
    private final AlipayClient alipayClient;

    aioveuAlipayGeneratePayParamsUtil aioveuAlipayGeneratePayParamsUtil;


    @Autowired
    public AlipayServiceImpl(AlipayRequestFactory requestFactory, AlipayConfig alipayConfig) {
        this.requestFactory = requestFactory;
        this.alipayConfig = alipayConfig;
        // 初始化AlipayClient
        this.alipayClient = new DefaultAlipayClient(
//                alipayConfig.getGatewayUrl(),
                alipayConfig.getAppId(),
//                alipayConfig.getPrivateKey(),
                alipayConfig.getFormat(),
                alipayConfig.getCharset(),
//                alipayConfig.getPublicKey(),
                alipayConfig.getSignType()
        );
    }

    /**
     * 支付宝成功响应码
     */
    private static final String ALIPAY_SUCCESS_CODE = "10000";

    /**
     * 日期格式化器
     */
    private static final SimpleDateFormat DATE_FORMATTER =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * APP支付
     */
    @Override
    public PaymentParamsVO appPay(PaymentRequestDTO request) {
        try {
            log.info("支付宝APP支付, 订单号: {}, 金额: {}",
                    request.getOrderNo(), request.getAmount());

            // 验证请求参数
            validatePaymentRequest(request);

            // 创建支付请求参数
            Map<String, String> params = requestFactory.createAppRequest(request, alipayConfig);
            log.debug("支付宝APP支付请求参数: {}", params);

            // 创建APP支付请求
            AlipayTradeAppPayRequest alipayRequest = new AlipayTradeAppPayRequest();
            alipayRequest.setBizContent(params.get("biz_content"));
            alipayRequest.setNotifyUrl(alipayConfig.getNotifyUrl());

            // 执行请求
            AlipayTradeAppPayResponse response = alipayClient.execute(alipayRequest);

            if (!ALIPAY_SUCCESS_CODE.equals(response.getCode())) {
                throw new RuntimeException(String.format("支付宝APP支付失败: %s - %s",
                        response.getSubCode(), response.getSubMsg()));
            }

            // 生成支付参数
            Map<String, Object> payParams = aioveuAlipayGeneratePayParamsUtil.generateAppPayParams(response.getBody());

            // 生成支付参数
            return PaymentParamsVO.builder()
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

        } catch (Exception e) {
            log.error("支付宝APP支付失败, 订单号: {}", request.getOrderNo(), e);
            throw new RuntimeException("支付宝APP支付失败: " + e.getMessage(), e);
        }
    }

    /**
     * 网页支付
     */
    @Override
    public PaymentParamsVO pagePay(PaymentRequestDTO request)  {
        try {
            log.info("支付宝网页支付, 订单号: {}, 金额: {}",
                    request.getOrderNo(), request.getAmount());

            // 验证请求参数
            validatePaymentRequest(request);

            // 创建支付请求参数
            Map<String, String> params = requestFactory.createPageRequest(request, alipayConfig);
            log.debug("支付宝网页支付请求参数: {}", params);

            // 创建网页支付请求
            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
            alipayRequest.setBizContent(params.get("biz_content"));
            alipayRequest.setReturnUrl(alipayConfig.getReturnUrl());
            alipayRequest.setNotifyUrl(alipayConfig.getNotifyUrl());

            // 执行请求
            AlipayTradePagePayResponse response = alipayClient.pageExecute(alipayRequest);

            if (!ALIPAY_SUCCESS_CODE.equals(response.getCode())) {
                throw new RuntimeException(String.format("支付宝网页支付失败: %s - %s",
                        response.getSubCode(), response.getSubMsg()));
            }

            // 生成支付参数
            Map<String, Object> payParams = aioveuAlipayGeneratePayParamsUtil.generateAppPayParams(response.getBody());

            // 生成支付参数
            return PaymentParamsVO.builder()
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

        } catch (Exception e) {
            log.error("支付宝网页支付失败, 订单号: {}", request.getOrderNo(), e);
            throw new RuntimeException("支付宝网页支付失败: " + e.getMessage(), e);
        }
    }

    /**
     * 手机网站支付
     */
    @Override
    public PaymentParamsVO wapPay(PaymentRequestDTO request)  {
        try {
            log.info("支付宝手机网站支付, 订单号: {}, 金额: {}",
                    request.getOrderNo(), request.getAmount());

            // 验证请求参数
            validatePaymentRequest(request);

            // 创建支付请求参数
            Map<String, String> params = requestFactory.createWapRequest(request, alipayConfig);
            log.debug("支付宝手机网站支付请求参数: {}", params);

            // 创建手机网站支付请求
            AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
            alipayRequest.setBizContent(params.get("biz_content"));
            alipayRequest.setReturnUrl(alipayConfig.getReturnUrl());
            alipayRequest.setNotifyUrl(alipayConfig.getNotifyUrl());

            // 执行请求
            AlipayTradeWapPayResponse response = alipayClient.pageExecute(alipayRequest);

            if (!ALIPAY_SUCCESS_CODE.equals(response.getCode())) {
                throw new RuntimeException(String.format("支付宝手机网站支付失败: %s - %s",
                        response.getSubCode(), response.getSubMsg()));
            }

            // 生成支付参数
            Map<String, Object> payParams = aioveuAlipayGeneratePayParamsUtil.generateAppPayParams(response.getBody());

            // 生成支付参数
            return PaymentParamsVO.builder()
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

        } catch (Exception e) {
            log.error("支付宝手机网站支付失败, 订单号: {}", request.getOrderNo(), e);
            throw new RuntimeException("支付宝手机网站支付失败: " + e.getMessage(), e);
        }
    }

    /**
     *   TODO   查询订单状态
     *              是的，必须调用支付宝客户端查询，因为：
     *              ✅ 状态同步：支付宝状态是最终状态
     *              ✅ 对账需要：确保数据库与支付宝状态一致
     *              ✅ 用户查询：用户可能主动查询支付结果
     *              ✅ 异常处理：处理未收到回调的情况
     *              ✅ 数据准确：保证数据准确性
     *              建议策略：
     *              回调为主：主要依赖支付宝回调更新状态
     *              查询为辅：对未收到回调的订单进行主动查询
     *              定时同步：定时同步处理中的订单状态
     *              缓存优化：对已完成的订单进行缓存，减少查询
     *              这样既保证了准确性，又优化了性能！🎉
     */
    @Override
    public PaymentStatusVO queryPayment(String paymentNo)  {
        try {
            log.info("查询支付宝订单状态, 订单号: {}", paymentNo);

            if (paymentNo == null || paymentNo.trim().isEmpty()) {
                throw new IllegalArgumentException("支付订单号不能为空");
            }

            // 创建查询请求参数
            Map<String, String> params = requestFactory.createQueryRequest(paymentNo, alipayConfig);
            log.debug("支付宝查询订单请求参数: {}", params);

            // 创建查询请求
            AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();
            alipayRequest.setBizContent(params.get("biz_content"));

            // 执行请求
            AlipayTradeQueryResponse response = alipayClient.execute(alipayRequest);

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
     */
    @Override
    public RefundResultVO refund(RefundRequestDTO request)  {
        try {
            log.info("支付宝退款, 退款单号: {}, 支付订单号: {}, 退款金额: {}",
                    request.getRefundNo(), request.getPaymentNo(), request.getRefundAmount());

            // 验证退款请求
            validateRefundRequest(request);

            // 创建退款请求参数
            Map<String, String> params = requestFactory.createRefundRequest(
                    request.getRefundNo(), request.getPaymentNo(),
                    request.getRefundAmount(), request.getRefundReason(), alipayConfig);
            log.debug("支付宝退款请求参数: {}", params);

            // 创建退款请求
            AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
            alipayRequest.setBizContent(params.get("biz_content"));

            // 执行请求
            AlipayTradeRefundResponse response = alipayClient.execute(alipayRequest);

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
     */
    @Override
    public boolean closePayment(String paymentNo)  {
        try {
            log.info("关闭支付宝订单, 订单号: {}", paymentNo);

            if (paymentNo == null || paymentNo.trim().isEmpty()) {
                throw new IllegalArgumentException("支付订单号不能为空");
            }

            // 创建关闭请求参数
            Map<String, String> params = requestFactory.createCloseRequest(paymentNo, alipayConfig);
            log.debug("支付宝关闭订单请求参数: {}", params);

            // 创建关闭请求
            AlipayTradeCloseRequest alipayRequest = new AlipayTradeCloseRequest();
            alipayRequest.setBizContent(params.get("biz_content"));

            // 执行请求
            AlipayTradeCloseResponse response = alipayClient.execute(alipayRequest);

            boolean success = ALIPAY_SUCCESS_CODE.equals(response.getCode());
            log.info("关闭支付宝订单结果, 订单号: {}, 成功: {}", paymentNo, success);

            return success;

        } catch (Exception e) {
            log.error("关闭支付宝订单失败, 订单号: {}", paymentNo, e);
            throw new RuntimeException("关闭支付宝订单失败: " + e.getMessage(), e);
        }
    }

    /**
     * 转换支付状态
     */
    private PaymentStatusVO convertToPaymentStatus(AlipayTradeQueryResponse response) {

        if (!ALIPAY_SUCCESS_CODE.equals(response.getCode())) {
            String errorMsg = String.format("查询失败: %s - %s",
                    response.getSubCode(), response.getSubMsg());
            throw new RuntimeException(errorMsg);
        }

        return PaymentStatusVO.builder()
                .paymentNo(response.getOutTradeNo())
                .thirdPaymentNo(response.getTradeNo())
                .amount(parseBigDecimal(response.getTotalAmount()))
                .paymentStatus(convertAlipayStatus(response.getTradeStatus()))
                .paymentTime(response.getSendPayDate())  // 直接使用Date类型
                .build();
    }

    /**
     * 转换退款结果
     */
    private RefundResultVO convertToRefundResult(RefundRequestDTO request,
                                                 AlipayTradeRefundResponse response) {

        if (!ALIPAY_SUCCESS_CODE.equals(response.getCode())) {
            String errorMsg = String.format("退款失败: %s - %s",
                    response.getSubCode(), response.getSubMsg());
            throw new RuntimeException(errorMsg);
        }

        return RefundResultVO.builder()
                .refundNo(request.getRefundNo())
                .thirdRefundNo(response.getTradeNo())
                .refundAmount(parseBigDecimal(response.getRefundFee()))
                .refundStatus(RefundStatusEnum.SUCCESS.getValue())
                .refundTime(new Date())
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
