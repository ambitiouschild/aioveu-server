package com.aioveu.pay.aioveuModule.service.MockPay.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName: MockProperties
 * @Description TODO  模拟支付配置属性
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/11 18:39
 * @Version 1.0
 **/

@Slf4j
@Data
//@Component  // ✅ 这里会创建一个Bean
@ConfigurationProperties(prefix = "pay.mock")
public class MockPayConfig {

    // 基本配置
    private Boolean enabled = true;
    private Boolean autoSuccess = true;
    private Integer delay = 1000;
    private Integer successRate = 100;

    // 微信支付配置
    private Wechat wechat = new Wechat();

    // 支付宝支付配置
    private Alipay alipay = new Alipay();

    // 余额支付配置
    private Balance balance = new Balance();

    // 调试配置
    private Boolean debug = true;
    private Boolean logRequest = true;
    private Boolean logResponse = true;

    // 微信配置内部类
    @Data
    public static class Wechat {
        private Boolean enabled = true;
        private String appId = "wx1234567890abcdef";
        private String mchId = "1230000109";
        private String notifyUrl = "http://localhost:8888/api/payment/callback/wechat";
    }

    // 支付宝配置内部类
    @Data
    public static class Alipay {
        private Boolean enabled = true;
        private String appId = "2021000118691234";
        private String notifyUrl = "http://localhost:8888/api/payment/callback/alipay";
    }

    // 余额配置内部类
    @Data
    public static class Balance {
        private Boolean enabled = true;
        private Long defaultBalance = 1000000L; // 10000元
    }

    @PostConstruct
    public void init() {
        log.info("模拟支付配置加载完成:");
        log.info("  启用状态: {}", enabled);
        log.info("  自动成功: {}", autoSuccess);
        log.info("  模拟延迟: {}ms", delay);
        log.info("  成功率: {}%", successRate);
        log.info("  微信模拟: {}", wechat.getEnabled());
        log.info("  支付宝模拟: {}", alipay.getEnabled());
        log.info("  余额模拟: {}", balance.getEnabled());
    }

    /**
     * 验证配置有效性
     */
    public boolean isValid() {
        if (enabled == null) {
            log.warn("模拟支付启用状态未配置，默认启用");
            enabled = true;
        }

        if (delay != null && delay < 0) {
            log.warn("模拟延迟不能为负数，已重置为0");
            delay = 0;
        }

        if (successRate != null && (successRate < 0 || successRate > 100)) {
            log.warn("模拟成功率必须在0-100之间，已重置为100");
            successRate = 100;
        }

        return true;
    }

    /**
     * 模拟支付是否成功
     */
    public boolean shouldSuccess() {
        if (Boolean.TRUE.equals(autoSuccess)) {
            return true;
        }

        if (successRate == null || successRate >= 100) {
            return true;
        }

        if (successRate <= 0) {
            return false;
        }

        return Math.random() * 100 < successRate;
    }
}
