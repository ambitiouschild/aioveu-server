package com.aioveu.oms.aioveu01Order.utils;

import com.github.binarywang.wxpay.service.WxPayService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName: WxPayConfigValidator
 * @Description TODO  添加配置验证Bean
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/23 12:39
 * @Version 1.0
 **/

@Slf4j
@Component
public class WxPayConfigValidator {

    @Autowired(required = false)
    private WxPayService wxPayService;

    @PostConstruct
    public void validate() {
        log.info("开始验证微信支付配置...");

        if (wxPayService == null) {
            log.error("❌ WxPayService Bean 未创建");
            return;
        }

        try {
            com.github.binarywang.wxpay.config.WxPayConfig config = wxPayService.getConfig();
            if (config == null) {
                log.error("❌ WxPayConfig 为 null");
                return;
            }

            log.info("✅ WxPayService 配置检查:");
            log.info("   配置类: {}", config.getClass().getName());
            log.info("   AppId: {}", config.getAppId());
            log.info("   MchId: {}", config.getMchId());
            log.info("   MchKey: {}", maskKey(config.getMchKey()));
            log.info("   SignType: {}", config.getSignType());
            log.info("   NotifyUrl: {}", config.getNotifyUrl());
            log.info("   UseSandboxEnv: {}", config.isUseSandboxEnv());

            // 通过反射检查configMap
            try {
                java.lang.reflect.Field configMapField = wxPayService.getClass().getDeclaredField("configMap");
                configMapField.setAccessible(true);
                Object configMap = configMapField.get(wxPayService);
                if (configMap == null) {
                    log.error("❌ configMap 为 null，配置未正确设置");
                } else {
                    log.info("✅ configMap 不为 null");
                }
            } catch (Exception e) {
                log.warn("无法检查configMap: {}", e.getMessage());
            }

        } catch (Exception e) {
            log.error("❌ 检查微信支付配置失败: {}", e.getMessage(), e);
        }
    }

    private String maskKey(String key) {
        if (key == null || key.length() <= 8) {
            return "***";
        }
        return key.substring(0, 4) + "***" + key.substring(key.length() - 4);
    }
}
