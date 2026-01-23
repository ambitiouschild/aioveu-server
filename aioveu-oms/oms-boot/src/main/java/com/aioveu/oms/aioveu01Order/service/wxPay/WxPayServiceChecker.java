package com.aioveu.oms.aioveu01Order.service.wxPay;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName: WxPayServiceChecker
 * @Description TODO  添加微信支付配置检查
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/23 12:04
 * @Version 1.0
 **/

@Component
@Slf4j
public class WxPayServiceChecker {

    @Autowired(required = false)
    private WxPayService wxPayService;

    @PostConstruct
    public void init() {
        checkWxPayService();
    }

    public void checkWxPayService() {
        if (wxPayService == null) {
            log.error("❌ 微信支付服务未注入，请检查配置");
            return;
        }

        try {
            WxPayConfig config = wxPayService.getConfig();
            if (config == null) {
                log.error("❌ 微信支付配置为空");
                return;
            }

            log.info("✅ 微信支付配置检查:");
            log.info("   AppId: {}", config.getAppId());
            log.info("   MchId: {}", config.getMchId());
            log.info("   NotifyUrl: {}", config.getNotifyUrl());
            log.info("   KeyPath: {}", config.getKeyPath());
            log.info("   SignType: {}", config.getSignType());

            // 测试调用
            String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
//            String nonceStr = WxPayUtil.generateNonceStr();
            log.info("✅ 微信支付服务初始化成功");

        } catch (Exception e) {
            log.error("❌ 微信支付服务检查失败", e);
        }
    }
}
