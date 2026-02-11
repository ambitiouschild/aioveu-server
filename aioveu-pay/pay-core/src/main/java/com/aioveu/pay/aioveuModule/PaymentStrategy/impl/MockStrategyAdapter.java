package com.aioveu.pay.aioveuModule.PaymentStrategy.impl;

import com.aioveu.pay.aioveuModule.PaymentStrategy.PaymentStrategy;
import com.aioveu.pay.aioveuModule.model.vo.*;
import com.aioveu.pay.aioveuModule.service.mock.service.MockPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: MockStrategyAdapter
 * @Description TODO  模拟支付策略适配器  实现 PaymentStrategy 接口  将模拟支付服务包装为 PaymentStrategy
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/11 18:52
 * @Version 1.0
 **/

@Slf4j
@Component
@Qualifier("mockPayStrategy")
public class MockStrategyAdapter implements PaymentStrategy {

    /**
     * 模拟支付服务
     */
    private final MockPayService mockPayService;


    /**
     * 支付类型映射
     */
    private static final Map<String, String> PAY_TYPE_MAPPING = new HashMap<>();

    static {
        // 支付类型映射
        PAY_TYPE_MAPPING.put("APP", "APP");
        PAY_TYPE_MAPPING.put("PAGE", "PAGE");
        PAY_TYPE_MAPPING.put("WAP", "WAP");
        PAY_TYPE_MAPPING.put("H5", "WAP");  // H5使用WAP支付
    }

    @Autowired
    public MockStrategyAdapter(MockPayService mockPayService) {
        this.mockPayService = mockPayService;
    }

    /**
     * 发起支付
     *
     * @param paymentNo 支付订单号
     * @param request   支付请求
     * @return 支付参数
     */
    @Override
    public PaymentParamsVO appPay(String paymentNo, PaymentRequestDTO request) {
        try {
            log.info("支付宝策略支付, 订单号: {}, 支付方式: {}, 金额: {}",
                    paymentNo, request.getPayType(), request.getAmount());

            // 设置订单号
            request.setOrderNo(paymentNo);

            // 确定支付类型
            String payType = determinePayType(request);

            // 根据支付类型调用不同的支付方法
            switch (payType) {
                case "MockWxPay":
                    return processMockWxPay(request);
                case "MockAlipay":
                    return processMockAlipay(request);
                case "MockBalancePay":
                    return processMockBalancePay(request);
                default:
                    throw new IllegalArgumentException("不支持的支付宝支付类型: " + payType);
            }

        } catch (Exception e) {
            log.error("支付宝策略支付失败, 订单号: {}, 支付类型: {}",
                    paymentNo, request.getPayType(), e);
            throw new RuntimeException("支付宝支付失败: " + e.getMessage(), e);
        }
    }

    /**
     * 处理Wx支付
     */
    private PaymentParamsVO processMockWxPay(PaymentRequestDTO request) {
        // 使用传统SDK
        return mockPayService.mockWxPay(request);

        // 或者使用EasySDK
        // return alipayEasyService.appPay(request);
    }

    /**
     * 处理网页支付
     */
    private PaymentParamsVO processMockAlipay(PaymentRequestDTO request) {
        // 使用传统SDK
        return mockPayService.mockAlipay(request);

        // 或者使用EasySDK
        // return alipayEasyService.pagePay(request);
    }

    /**
     * 处理模拟余额支付
     */
    private PaymentParamsVO processMockBalancePay(PaymentRequestDTO request) {
        // 使用传统SDK
        return mockPayService.mockBalancePay(request);

        // 或者使用EasySDK
        // return alipayEasyService.wapPay(request);
    }

    /**
     * 根据请求参数确定支付类型
     */
    private String determinePayType(PaymentRequestDTO request) {
        String payType = request.getPayType();

        if (payType == null || payType.trim().isEmpty()) {
            // 根据设备类型自动判断
            return autoDetectPayType(request);
        }

        // 从映射中获取标准支付类型
        String standardPayType = PAY_TYPE_MAPPING.get(payType.toUpperCase());

        if (standardPayType == null) {
            log.warn("未知的支付类型: {}, 使用默认APP支付", payType);
            return "APP";
        }

        return standardPayType;
    }

    /**
     * 自动检测支付类型
     */
    private String autoDetectPayType(PaymentRequestDTO request) {
        // 这里可以根据设备类型、用户代理等自动判断
        String userAgent = request.getUserAgent();

        if (userAgent != null) {
            userAgent = userAgent.toLowerCase();

            if (userAgent.contains("micromessenger")) {
                // 微信内，使用H5支付
                return "WAP";
            } else if (userAgent.contains("alipay")) {
                // 支付宝内，使用APP支付
                return "APP";
            } else if (userAgent.contains("mobile") ||
                    userAgent.contains("android") ||
                    userAgent.contains("iphone")) {
                // 移动设备，使用WAP支付
                return "WAP";
            }
        }

        // 默认使用网页支付
        return "PAGE";
    }

    /**
     * 验证回调
     *
     * @param callback 回调数据
     * @return 验证结果
     */
    @Override
    public boolean verifyCallback(PaymentCallbackDTO callback) {
        try {
            log.info("支付宝回调验证, 订单号: {}, 支付宝交易号: {}",
                    callback.getPaymentNo(), callback.getThirdPaymentNo());

            // 这里可以实现支付宝回调签名验证
            // 通常支付宝回调验证在回调Controller中实现
            // 这里简单返回true，实际应该根据业务需求实现

            return true;

        } catch (Exception e) {
            log.error("支付宝回调验证失败, 订单号: {}", callback.getPaymentNo(), e);
            return false;
        }
    }

    /**
     * 查询支付状态
     *
     * @param paymentNo 支付订单号
     * @return 支付状态
     */
    @Override
    public PaymentStatusVO queryStatus(String paymentNo) {
        try {
            log.info("支付宝查询支付状态, 订单号: {}", paymentNo);

            // 使用传统SDK查询
            return mockPayService.queryPayment(paymentNo);

            // 或者使用EasySDK查询
            // return alipayEasyService.queryPayment(paymentNo);

        } catch (Exception e) {
            log.error("支付宝查询支付状态失败, 订单号: {}", paymentNo, e);
            throw new RuntimeException("查询支付宝支付状态失败: " + e.getMessage(), e);
        }
    }

    /**
     * 关闭支付订单
     *
     * @param paymentNo 支付订单号
     * @return 关闭结果
     */
    @Override
    public boolean closePayment(String paymentNo) {
        try {
            log.info("支付宝关闭支付订单, 订单号: {}", paymentNo);

            // 使用传统SDK关闭订单
            return mockPayService.closePayment(paymentNo);

            // 或者使用EasySDK关闭订单
            // return alipayEasyService.closePayment(paymentNo);

        } catch (Exception e) {
            log.error("支付宝关闭支付订单失败, 订单号: {}", paymentNo, e);
            throw new RuntimeException("关闭支付宝支付订单失败: " + e.getMessage(), e);
        }
    }

    /**
     * 退款
     *
     * @param refundNo 退款单号
     * @param request  退款请求
     * @return 退款结果
     */
    @Override
    public RefundResultVO refund(String refundNo, RefundRequestDTO request) {
        try {
            log.info("支付宝退款, 退款单号: {}, 支付订单号: {}, 退款金额: {}",
                    refundNo, request.getPaymentNo(), request.getRefundAmount());

            // 设置退款单号
            request.setRefundNo(refundNo);

            // 使用传统SDK退款
            return mockPayService.refund(request);

            // 或者使用EasySDK退款
            // return alipayEasyService.refund(request);

        } catch (Exception e) {
            log.error("支付宝退款失败, 退款单号: {}, 支付订单号: {}",
                    refundNo, request.getPaymentNo(), e);
            throw new RuntimeException("支付宝退款失败: " + e.getMessage(), e);
        }
    }


    /**
     * 获取支持的所有支付类型
     *
     * @return 支持支付类型数组
     */
    public String[] getSupportedPayTypes() {
        return new String[]{"APP", "PAGE", "WAP"};
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

        if (request.getAmount() == null || request.getAmount().doubleValue() <= 0) {
            throw new IllegalArgumentException("支付金额必须大于0");
        }

        if (request.getSubject() == null || request.getSubject().trim().isEmpty()) {
            throw new IllegalArgumentException("订单标题不能为空");
        }
    }

    /**
     * 异步通知处理
     *
     * @param notifyParams 通知参数
     * @return 处理结果
     */
    public boolean processAsyncNotify(Map<String, String> notifyParams) {
        try {
            log.info("处理支付宝异步通知, 参数: {}", notifyParams);

            // 这里可以实现支付宝异步通知的业务处理逻辑
            // 例如：更新订单状态、记录支付日志等

            String tradeNo = notifyParams.get("trade_no");
            String outTradeNo = notifyParams.get("out_trade_no");
            String tradeStatus = notifyParams.get("trade_status");

            log.info("支付宝异步通知处理完成, 订单号: {}, 支付宝交易号: {}, 状态: {}",
                    outTradeNo, tradeNo, tradeStatus);

            return true;

        } catch (Exception e) {
            log.error("处理支付宝异步通知失败", e);
            return false;
        }
    }
}
