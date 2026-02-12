package com.aioveu.pay.aioveuModule.service.MockPay.service.impl;

import com.aioveu.pay.aioveuModule.enums.PaymentStatusEnum;
import com.aioveu.pay.aioveuModule.enums.RefundStatusEnum;
import com.aioveu.pay.aioveuModule.model.vo.*;
import com.aioveu.pay.aioveuModule.service.MockPay.MockRequestFactory.MockRequestFactory;
import com.aioveu.pay.aioveuModule.service.MockPay.config.MockPayConfig;
import com.aioveu.pay.aioveuModule.service.MockPay.service.MockPayService;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import static com.fasterxml.jackson.core.io.NumberInput.parseBigDecimal;


/**
 * @ClassName: MockPayServiceImpl
 * @Description TODO  模拟支付服务实现  - 返回支付参数
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/11 18:43
 * @Version 1.0
 **/

@Slf4j
@Service
public class MockPayServiceImpl implements MockPayService {

    private final MockRequestFactory requestFactory;


    private final MockPayConfig mockPayConfig;
    private final Random random = new Random();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public MockPayServiceImpl(MockRequestFactory requestFactory,
                              MockPayConfig mockPayConfig) {
        this.requestFactory = requestFactory;
        this.mockPayConfig = mockPayConfig;
        log.info("模拟支付服务初始化完成");
    }

    @Override
    public PaymentParamsVO mockWxPay(PaymentRequestDTO request) {
        if (!mockPayConfig.getEnabled()) {
            throw new RuntimeException("模拟支付未启用");
        }

        if (!mockPayConfig.getWechat().getEnabled()) {
            throw new RuntimeException("微信模拟支付未启用");
        }

        String orderNo = request.getOrderNo();
        BigDecimal amount = request.getAmount();
        String openId = request.getOpenId();

        // 记录请求
        if (mockPayConfig.getLogRequest()) {
            log.info("[微信模拟支付] 订单: {}, 金额: {}分, OpenID: {}",
                    orderNo, amount, openId);
        }

        // 模拟延迟
        simulateDelay();

        // 模拟支付结果
        boolean success = mockPayConfig.shouldSuccess();

        // 生成支付参数
        return buildWxPaymentParams(orderNo, amount, request.getOpenId());
    }

    @Override
    public PaymentParamsVO mockAlipay(PaymentRequestDTO request) {
        if (!mockPayConfig.getEnabled()) {
            throw new RuntimeException("模拟支付未启用");
        }

        if (!mockPayConfig.getAlipay().getEnabled()) {
            throw new RuntimeException("支付宝模拟支付未启用");
        }

        String orderNo = request.getOrderNo();
        BigDecimal amount = request.getAmount();
        String openId = request.getOpenId();

        // 记录请求
        if (mockPayConfig.getLogRequest()) {
            log.info("[支付宝模拟支付] 订单: {}, 金额: {}分", orderNo, amount);
        }

        // 模拟延迟
        simulateDelay();

        // 模拟支付结果
        boolean success = mockPayConfig.shouldSuccess();

        // 生成支付参数
        return buildAlipayPaymentParams(orderNo, amount, request.getSubject(), request.getBody());
    }

    @Override
    public PaymentParamsVO mockBalancePay(PaymentRequestDTO request) {
        if (!mockPayConfig.getEnabled()) {
            throw new RuntimeException("模拟支付未启用");
        }

        if (!mockPayConfig.getBalance().getEnabled()) {
            throw new RuntimeException("余额模拟支付未启用");
        }

        String orderNo = request.getOrderNo();
        BigDecimal amount = request.getAmount();
        Long userId = request.getUserId();

        // 记录请求
        if (mockPayConfig.getLogRequest()) {
            log.info("[余额模拟支付] 订单: {}, 金额: {}分, 用户: {}", orderNo, amount, userId);
        }

        // 模拟延迟
        simulateDelay();


        // 生成支付参数
        return buildBalancePaymentParams(orderNo, amount, userId);
    }

    private void simulateDelay() {
        if (mockPayConfig.getDelay() > 0) {
            try {
                Thread.sleep(mockPayConfig.getDelay());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 生成微信支付参数
     */
    private PaymentParamsVO buildWxPaymentParams(String orderNo, BigDecimal amount, String openId) {
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonceStr = generateNonceStr(32);
        String prepayId = "wx" + System.currentTimeMillis() + random.nextInt(1000);
        String packageValue = "prepay_id=" + prepayId;
        String appId = mockPayConfig.getWechat().getAppId();

        // 模拟签名
        String signType = "MD5";
        String paySign = generateMockSign(64);

        // 构建支付参数
        PaymentParamsVO params = new PaymentParamsVO();
        params.setPaymentNo("WX" + generatePaymentNo());
        params.setOrderNo(orderNo);
        params.setChannel("wechat");
        params.setSuccess(true);
        params.setMessage("微信支付参数生成成功");

        // 微信支付特定参数
        params.setAppId(appId);
        params.setTimeStamp(timeStamp);
        params.setNonceStr(nonceStr);
        params.setPackageValue(packageValue);
        params.setSignType(signType);
        params.setPaySign(paySign);
        params.setPrepayId(prepayId);
        params.setMchId(mockPayConfig.getWechat().getMchId());

        // 生成调起支付的参数字符串
        String paymentParams = buildWxPaymentParamsString(appId, timeStamp, nonceStr, packageValue, signType, paySign);
        params.setPaymentParams(paymentParams);

        // 其他信息
//        params.setSubject(properties.getWechat().getSubject());
//        params.setBody(properties.getWechat().getBody());
//        params.setTotalAmount(amount.toString());
        params.setCreateTime(System.currentTimeMillis());
        params.setExpireMinutes(30); // 30分钟过期

        if (mockPayConfig.getLogResponse()) {
            log.info("[微信支付参数] 订单: {}, prepayId: {}, 有效期: {}分钟",
                    orderNo, prepayId, params.getExpireMinutes());
        }

        return params;
    }

    /**
     * 生成微信支付参数字符串
     */
    private String buildWxPaymentParamsString(String appId, String timeStamp, String nonceStr,
                                              String packageValue, String signType, String paySign) {
        return String.format("appId=%s&timeStamp=%s&nonceStr=%s&package=%s&signType=%s&paySign=%s",
                appId, timeStamp, nonceStr, packageValue, signType, paySign);
    }

    /**
     * 生成支付宝支付参数
     */
    private PaymentParamsVO buildAlipayPaymentParams(String orderNo, BigDecimal amount,
                                                     String subject, String body) {
        String appId = mockPayConfig.getAlipay().getAppId();
        String tradeNo = "ali" + System.currentTimeMillis() + random.nextInt(1000);


        // 构建支付参数
        PaymentParamsVO params = new PaymentParamsVO();
        params.setPaymentNo("ALI" + generatePaymentNo());
        params.setOrderNo(orderNo);
        params.setChannel("alipay");
        params.setSuccess(true);
        params.setMessage("支付宝支付参数生成成功");

        // 支付宝支付特定参数
        params.setAppId(appId);
        params.setTradeNo(tradeNo);
        params.setOutTradeNo(orderNo);

        // 支付参数字符串


        // 其他信息
        params.setSubject(subject);
        params.setBody(body);
        params.setCreateTime(System.currentTimeMillis());
        params.setExpireMinutes(15); // 支付宝15分钟过期

        if (mockPayConfig.getLogResponse()) {
            log.info("[支付宝支付参数] 订单: {}, tradeNo: {}, 有效期: {}分钟",
                    orderNo, tradeNo, params.getExpireMinutes());
        }

        return params;
    }



    /**
     * 生成余额支付参数
     */
    private PaymentParamsVO buildBalancePaymentParams(String orderNo, BigDecimal amount, Long userId) {
        // 检查余额是否充足

        // 构建支付参数
        PaymentParamsVO params = new PaymentParamsVO();
        params.setPaymentNo("BAL" + generatePaymentNo());
        params.setOrderNo(orderNo);
        params.setChannel("balance");
        params.setSuccess(true);
        params.setMessage("余额支付参数生成成功");

        // 余额支付特定信息
        params.setUserId(userId);

        // 余额支付不需要额外的支付参数
        params.setPaymentParams(orderNo + "|" + userId + "|" + amount);

        // 计算剩余余额


        // 其他信息
        params.setSubject("余额支付");
        params.setBody("用户余额支付");
        params.setCreateTime(System.currentTimeMillis());
        params.setExpireMinutes(5); // 余额支付5分钟过期

        if (mockPayConfig.getLogResponse()) {
            log.info("[余额支付参数] 订单: {}, 用户: {}, 支付金额: {}元, 剩余余额: {}元",
                    orderNo, userId, amount);
        }

        return params;
    }


    /**
     * 生成随机字符串
     */
    private String generateNonceStr(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * 生成模拟签名
     */
    private String generateMockSign(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * 生成支付单号
     */
    private String generatePaymentNo() {
        return LocalDateTime.now().format(FORMATTER) +
                String.format("%06d", random.nextInt(1000000));
    }


    /**

     */
    @Override
    public PaymentStatusVO queryPayment(String paymentNo)  {
        try {
            log.info("查询支付宝订单状态, 订单号: {}", paymentNo);

            if (paymentNo == null || paymentNo.trim().isEmpty()) {
                throw new IllegalArgumentException("支付订单号不能为空");
            }

            // 创建查询请求参数
            Map<String, String> params = requestFactory.createQueryRequest(paymentNo, mockPayConfig);
            log.debug("支付宝查询订单请求参数: {}", params);

            // 创建查询请求
            AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();
            alipayRequest.setBizContent(params.get("biz_content"));

            // 执行请求
//            AlipayTradeQueryResponse response = alipayClient.execute(alipayRequest);
            AlipayTradeQueryResponse response = new AlipayTradeQueryResponse();

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
                    request.getRefundAmount(), request.getRefundReason(), mockPayConfig);
            log.debug("支付宝退款请求参数: {}", params);

            // 创建退款请求
            AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
            alipayRequest.setBizContent(params.get("biz_content"));

            // 执行请求
//            AlipayTradeRefundResponse response = alipayClient.execute(alipayRequest);

            AlipayTradeRefundResponse response = new AlipayTradeRefundResponse();

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
            Map<String, String> params = requestFactory.createCloseRequest(paymentNo, mockPayConfig);
            log.debug("支付宝关闭订单请求参数: {}", params);

            // 创建关闭请求
            AlipayTradeCloseRequest alipayRequest = new AlipayTradeCloseRequest();
            alipayRequest.setBizContent(params.get("biz_content"));

            // 执行请求
//            AlipayTradeCloseResponse response = alipayClient.execute(alipayRequest);

            AlipayTradeQueryResponse response = new AlipayTradeQueryResponse();

            boolean success = true;
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

        if (!true) {
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

        if (!true) {
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
}
