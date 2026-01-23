package com.aioveu.oms.config;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: TODO 微信支付配置  - 支持模拟支付
 * @Author: 雒世松
 * @Date: 2025/6/5 18:03
 * @param
 * @return:
 **/

@Slf4j
@Configuration
@ConditionalOnClass(WxPayService.class)
@EnableConfigurationProperties(WxPayProperties.class)
@AllArgsConstructor
public class WxPayConfiguration {

    private final WxPayProperties properties;

    @PostConstruct
    public void init() {
        log.info("========== 微信支付配置初始化检查 ==========");
        log.info("AppId: {}", StringUtils.isNotBlank(properties.getAppId()) ? "已配置" : "❌ 未配置");
        log.info("MchId: {}", StringUtils.isNotBlank(properties.getMchId()) ? "已配置" : "❌ 未配置");
        log.info("MchKey: {}", StringUtils.isNotBlank(properties.getMchKey()) ? "已配置" : "❌ 未配置");

        // 检查通知URL
        String notifyUrl = properties.getNotifyUrl();
        if (StringUtils.isBlank(notifyUrl)) {
            notifyUrl = properties.getPayNotifyUrl();
        }
        log.info("NotifyUrl: {}", StringUtils.isNotBlank(notifyUrl) ? notifyUrl : "❌ 未配置");

        log.info("SignType: {}", StringUtils.defaultIfBlank(properties.getSignType(), "MD5"));
        log.info("SandboxEnabled: {}", BooleanUtils.toBoolean(properties.getSandboxEnabled()));
        log.info("==========================================");

    }

    @Bean
    @ConditionalOnMissingBean
    public WxPayService wxPayService() {


        log.info("创建WxPayService Bean...");

        WxPayService wxPayService = new WxPayServiceImpl();
        WxPayConfig payConfig = new WxPayConfig();

        // 设置必需参数
        String appId = StringUtils.trimToNull(properties.getAppId());
        String mchId = StringUtils.trimToNull(properties.getMchId());
        String mchKey = StringUtils.trimToNull(properties.getMchKey());

        if (StringUtils.isBlank(appId)) {
            log.error("❌ 微信支付AppId未配置，支付功能将不可用！");
        } else {
            payConfig.setAppId(appId);
        }

        if (StringUtils.isBlank(mchId)) {
            log.error("❌ 微信支付商户号未配置，支付功能将不可用！");
        } else {
            payConfig.setMchId(mchId);
        }

        if (StringUtils.isBlank(mchKey)) {
            log.error("❌ 微信支付商户密钥未配置，支付功能将不可用！");
        } else {
            payConfig.setMchKey(mchKey);
        }

        // 设置通知URL（优先使用notifyUrl，兼容payNotifyUrl）
        String notifyUrl = StringUtils.trimToNull(properties.getNotifyUrl());
        if (StringUtils.isBlank(notifyUrl)) {
            notifyUrl = StringUtils.trimToNull(properties.getPayNotifyUrl());
        }

        if (StringUtils.isNotBlank(notifyUrl)) {
            payConfig.setNotifyUrl(notifyUrl);
        } else {
            log.warn("⚠️ 微信支付通知URL未配置，支付回调将无法接收");
        }

        // 设置签名类型
        String signType = StringUtils.trimToNull(properties.getSignType());
        if (StringUtils.isNotBlank(signType)) {
            payConfig.setSignType(signType);
        }


        // 设置可选参数
        payConfig.setSubAppId(StringUtils.trimToNull(properties.getSubAppId()));
        payConfig.setSubMchId(StringUtils.trimToNull(properties.getSubMchId()));
        payConfig.setKeyPath(StringUtils.trimToNull(properties.getKeyPath()));
        payConfig.setApiV3Key(StringUtils.trimToNull(properties.getApiV3Key()));
        payConfig.setCertSerialNo(StringUtils.trimToNull(properties.getCertSerialNo()));
        payConfig.setPrivateKeyPath(StringUtils.trimToNull(properties.getPrivateKeyPath()));
        payConfig.setPrivateCertPath(StringUtils.trimToNull(properties.getPrivateCertPath()));


        // 设置沙箱环境
        payConfig.setUseSandboxEnv(BooleanUtils.toBoolean(properties.getSandboxEnabled()));

        // 验证必需配置
        if (StringUtils.isBlank(appId) || StringUtils.isBlank(mchId) || StringUtils.isBlank(mchKey)) {
            log.error("❌ 微信支付必需配置缺失，WxPayService将无法正常工作");
        } else {
            log.info("✅ WxPayService配置完成，AppId: {}, MchId: {}",
                    maskString(appId, 4), maskString(mchId, 4));
        }

        wxPayService.setConfig(payConfig);
        return wxPayService;


    }

    /**
     * 隐藏敏感信息
     */
    private String maskString(String str, int visibleChars) {
        if (StringUtils.isBlank(str) || str.length() <= visibleChars * 2) {
            return str;
        }
        int length = str.length();
        return str.substring(0, visibleChars) + "***" + str.substring(length - visibleChars);
    }

}
