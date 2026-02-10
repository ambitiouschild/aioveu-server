package com.aioveu.pay.aioveuModule.service.AliPay.AlipayRequestFactory;

import com.aioveu.pay.aioveuModule.model.vo.PaymentRequestDTO;
import com.aioveu.pay.aioveuModule.service.AliPay.config.AlipayConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: AlipayRequestFactory
 * @Description TODO 支付宝支付请求工厂
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 23:24
 * @Version 1.0
 **/

@Slf4j
@Component
public class AlipayRequestFactory {

    /**
     * 创建APP支付请求
     */
    public Map<String, String> createAppRequest(PaymentRequestDTO request, AlipayConfig config) {
        Map<String, String> params = new HashMap<>();

        // 公共参数
        setCommonParams(params, request, config);

        // APP支付特有参数
        params.put("method", "alipay.trade.app.pay");
        params.put("product_code", "QUICK_MSECURITY_PAY");

        return params;
    }

    /**
     * 创建网页支付请求
     */
    public Map<String, String> createPageRequest(PaymentRequestDTO request, AlipayConfig config) {
        Map<String, String> params = new HashMap<>();

        // 公共参数
        setCommonParams(params, request, config);

        // 网页支付特有参数
        params.put("method", "alipay.trade.page.pay");
        params.put("product_code", "FAST_INSTANT_TRADE_PAY");
        params.put("return_url", config.getReturnUrl());

        return params;
    }

    /**
     * 创建手机网站支付请求
     */
    public Map<String, String> createWapRequest(PaymentRequestDTO request, AlipayConfig config) {
        Map<String, String> params = new HashMap<>();

        // 公共参数
        setCommonParams(params, request, config);

        // 手机网站支付特有参数
        params.put("method", "alipay.trade.wap.pay");
        params.put("product_code", "QUICK_WAP_WAY");
        params.put("return_url", config.getReturnUrl());
//        params.put("quit_url", config.getQuitUrl());

        return params;
    }

    /**
     * 创建查询订单请求
     */
    public Map<String, String> createQueryRequest(String paymentNo, AlipayConfig config) {
        Map<String, String> params = new HashMap<>();

        // 公共参数
        setCommonParams(params, null, config);

        // 查询特有参数
        params.put("method", "alipay.trade.query");
        params.put("out_trade_no", paymentNo);

        return params;
    }

    /**
     * 创建退款请求
     */
    public Map<String, String> createRefundRequest(String refundNo, String paymentNo,
                                                   BigDecimal refundAmount, String refundReason,
                                                   AlipayConfig config) {
        Map<String, String> params = new HashMap<>();

        // 公共参数
        setCommonParams(params, null, config);

        // 退款特有参数
        params.put("method", "alipay.trade.refund");
        params.put("out_trade_no", paymentNo);
        params.put("out_request_no", refundNo);
        params.put("refund_amount", refundAmount.toString());
        params.put("refund_reason", refundReason);

        return params;
    }

    /**
     * 创建关闭订单请求
     */
    public Map<String, String> createCloseRequest(String paymentNo, AlipayConfig config) {
        Map<String, String> params = new HashMap<>();

        // 公共参数
        setCommonParams(params, null, config);

        // 关闭订单特有参数
        params.put("method", "alipay.trade.close");
        params.put("out_trade_no", paymentNo);

        return params;
    }

    /**
     * 设置公共参数
     */
    private void setCommonParams(Map<String, String> params, PaymentRequestDTO request, AlipayConfig config) {
        // 系统参数
        params.put("app_id", config.getAppId());
        params.put("format", "JSON");
        params.put("charset", "utf-8");
        params.put("sign_type", "RSA2");
        params.put("version", "1.0");
        params.put("timestamp", getCurrentTimestamp());
        params.put("notify_url", config.getNotifyUrl());

        // 业务参数 - 只有在支付请求时才设置
        if (request != null) {
            Map<String, Object> bizContent = new HashMap<>();
            bizContent.put("out_trade_no", request.getOrderNo());
            bizContent.put("total_amount", request.getAmount().toString());
            bizContent.put("subject", request.getSubject());
            bizContent.put("body", request.getBody());
//            bizContent.put("timeout_express", config.getTimeoutExpress());

            params.put("biz_content", toJson(bizContent));
        }
    }

    /**
     * 创建EasySDK支付请求
     */
    public com.alipay.easysdk.payment.app.Client createEasyAppClient() {
        return com.alipay.easysdk.factory.Factory.getAppClient();
    }

    public com.alipay.easysdk.payment.page.Client createEasyPageClient() {
        return com.alipay.easysdk.factory.Factory.getPageClient();
    }

    public com.alipay.easysdk.payment.wap.Client createEasyWapClient() {
        return com.alipay.easysdk.factory.Factory.getWapClient();
    }

    public com.alipay.easysdk.payment.common.Client createEasyCommonClient() {
        return com.alipay.easysdk.factory.Factory.getCommonClient();
    }

    /**
     * 创建统一订单模型（用于EasySDK）
     */
    public String createEasyOrderModel(PaymentRequestDTO request, String productCode) {
        Map<String, Object> order = new HashMap<>();
        order.put("subject", request.getSubject());
        order.put("out_trade_no", request.getOrderNo());
        order.put("total_amount", request.getAmount().toString());
        order.put("product_code", productCode);

        if (request.getBody() != null) {
            order.put("body", request.getBody());
        }

        return toJson(order);
    }

    /**
     * 工具方法 - 获取当前时间戳
     */
    private String getCurrentTimestamp() {
        return java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 工具方法 - 对象转JSON
     */
    private String toJson(Object obj) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            log.error("对象转JSON失败", e);
            return "{}";
        }
    }
}
