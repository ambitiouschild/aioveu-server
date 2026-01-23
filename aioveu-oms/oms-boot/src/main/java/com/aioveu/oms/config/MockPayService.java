package com.aioveu.oms.config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @ClassName: MockPayService
 * @Description TODO  模拟支付服务
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/23 12:52
 * @Version 1.0
 **/

@Slf4j
@Service
public class MockPayService {

    private final MockProperties properties;
    private final Random random = new Random();

    public MockPayService(MockProperties properties) {
        this.properties = properties;
        log.info("模拟支付服务初始化，enabled: {}, autoSuccess: {}, successRate: {}%",
                properties.getEnabled(), properties.getAutoSuccess(), properties.getSuccessRate());
    }

    /**
     * 检查是否启用模拟支付
     */
    public boolean isMockEnabled() {
        return Boolean.TRUE.equals(properties.getEnabled());
    }

    /**
     * 微信JSAPI模拟支付
     */
    public Map<String, Object> mockWxJsapiPay(String orderSn, Long paymentAmount) {
        log.info("【模拟支付】微信JSAPI支付，订单号: {}, 金额: {}分", orderSn, paymentAmount/ 100.0);


        double amountYuan = paymentAmount / 100.00;
        // 模拟延迟
        mockDelay();

        // 检查是否自动成功
        boolean success = checkSuccess();

        Map<String, Object> result = new HashMap<>();

        if (success) {
            // 模拟支付成功
            Map<String, String> payParams = generateMockWxPayParams(orderSn);

            result.put("success", true);
            result.put("code", "SUCCESS");
            result.put("message", "模拟支付成功");
            result.put("data", Map.of(
                    "payParams", payParams,
                    "orderSn", orderSn,
                    "paymentAmount", paymentAmount,   // 返回分
                    "amountYuan", amountYuan,             // 同时返回元，方便前端显示
                    "prepayId", payParams.get("prepayId"),
                    "mock", true
            ));

            log.info("【模拟支付】成功生成支付参数，订单号: {}", orderSn);
        } else {
            // 模拟支付失败
            result.put("success", false);
            result.put("code", "PAY_FAILED");
            result.put("message", "模拟支付失败（随机失败测试）");
            result.put("data", Map.of(
                    "orderSn", orderSn,
                    "mock", true
            ));

            log.warn("【模拟支付】模拟支付失败，订单号: {}", orderSn);
        }

        return result;
    }

    /**
     * 支付宝模拟支付
     */
    public Map<String, Object> mockAlipayPay(String orderSn, Long paymentAmount) {
        log.info("【模拟支付】支付宝支付，订单号: {}, 金额: {}分", orderSn, paymentAmount);

        mockDelay();
        boolean success = checkSuccess();

        Map<String, Object> result = new HashMap<>();

        if (success) {
            result.put("success", true);
            result.put("code", "SUCCESS");
            result.put("message", "模拟支付宝支付成功");
            result.put("data", Map.of(
                    "orderString", "mock_alipay_order_string_" + orderSn,
                    "orderSn", orderSn,
                    "mock", true
            ));
        } else {
            result.put("success", false);
            result.put("code", "PAY_FAILED");
            result.put("message", "模拟支付宝支付失败");
        }

        return result;
    }

    /**
     * 余额模拟支付
     */
    public Map<String, Object> mockBalancePay(String orderSn, Long paymentAmount, Long memberId) {
        log.info("【模拟支付】余额支付，订单号: {}, 金额: {}分, 会员ID: {}",
                orderSn, paymentAmount, memberId);

        mockDelay();
        boolean success = checkSuccess();

        Map<String, Object> result = new HashMap<>();

        if (success) {
            result.put("success", true);
            result.put("code", "SUCCESS");
            result.put("message", "模拟余额支付成功");
            result.put("data", Map.of(
                    "orderSn", orderSn,
                    "paymentAmount", paymentAmount,
                    "memberId", memberId,
                    "balanceAfter", 10000,  // 模拟剩余余额
                    "mock", true
            ));
        } else {
            result.put("success", false);
            result.put("code", "BALANCE_INSUFFICIENT");
            result.put("message", "模拟余额不足");
        }

        return result;
    }

    /**
     * 生成模拟的微信支付参数
     */
    private Map<String, String> generateMockWxPayParams(String orderSn) {
        Map<String, String> params = new HashMap<>();

        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonceStr = "mock_nonce_" + System.currentTimeMillis();
        String prepayId = "mock_prepay_" + orderSn + "_" + System.currentTimeMillis();
        String packageStr = "prepay_id=" + prepayId;
        String signType = "MD5";

        // 生成模拟签名
        String signData = String.format("appId=mock_app&timeStamp=%s&nonceStr=%s&package=%s&signType=%s&key=mock_key_123456",
                timeStamp, nonceStr, packageStr, signType);
        String paySign = generateMockSign(signData);

        params.put("timeStamp", timeStamp);
        params.put("nonceStr", nonceStr);
        params.put("package", packageStr);
        params.put("signType", signType);
        params.put("paySign", paySign);
        params.put("prepayId", prepayId);
        params.put("appId", "mock_app_id_" + orderSn.substring(0, 8));

        return params;
    }

    /**
     * 生成模拟签名
     */
    private String generateMockSign(String data) {
        try {
            // 简单MD5模拟
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(data.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString().toUpperCase();
        } catch (Exception e) {
            return "MOCK_SIGN_" + System.currentTimeMillis();
        }
    }

    /**
     * 模拟网络延迟
     */
    private void mockDelay() {
        if (properties.getDelay() != null && properties.getDelay() > 0) {
            try {
                Thread.sleep(properties.getDelay());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 检查是否成功
     */
    private boolean checkSuccess() {
        if (Boolean.TRUE.equals(properties.getAutoSuccess())) {
            return true;
        }

        if (properties.getSuccessRate() != null) {
            int randomValue = random.nextInt(100);
            return randomValue < properties.getSuccessRate();
        }

        return true;
    }
}
