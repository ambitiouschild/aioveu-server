package com.aioveu.pay.aioveuModule.service.WechatPay.configuration;

import cn.hutool.core.io.FileUtil;
import com.aioveu.pay.aioveuModule.service.WechatPay.config.WeChatPayConfig;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * @ClassName: WechatPayConfiguration
 * @Description TODO 微信支付客户端配置  这是后端配置，不是前端配置
 *                      用于后端与微信支付API通信
 *                      创建微信支付的服务客户端
 *                      处理支付回调、签名验证等
 *                      作用：Spring配置，创建和管理Bean
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 18:36
 * @Version 1.0
 **/

@Configuration
@Slf4j
public class WeChatPayConfiguration {

    @Autowired
    private WeChatPayConfig wechatPayConfig;

    @PostConstruct
    public void init() {
        log.info("微信支付配置初始化 - enabled: {}, appId: {}, mchId: {}",
                wechatPayConfig.isEnabled(),
                wechatPayConfig.getAppId(),
                wechatPayConfig.getMchId());
    }

    /**
     * JSAPI支付服务
     */
    @Bean
    public com.wechat.pay.java.service.payments.jsapi.JsapiService jsapiService() {
        // 检查是否启用
        if (!wechatPayConfig.isEnabled()) {
            log.info("微信支付已禁用，跳过初始化jsapiService");
            return null;
        }

        // 检查必要配置
        if (StringUtils.isBlank(wechatPayConfig.getAppId()) ||
                StringUtils.isBlank(wechatPayConfig.getMchId()) ||
                StringUtils.isBlank(wechatPayConfig.getApiV3Key())) {
            log.warn("微信支付必要配置缺失，跳过初始化jsapiService");
            return null;
        }

        try {
            com.wechat.pay.java.core.Config config = createConfig();
            if (config == null) {
                return null;
            }

            return new com.wechat.pay.java.service.payments.jsapi.JsapiService.Builder()
                    .config(config)
                    .build();
        } catch (Exception e) {
            log.error("初始化微信支付JsapiService失败，跳过", e);
            return null;
        }
    }

    /**
     * APP支付服务
     */
    @Bean
    public com.wechat.pay.java.service.payments.app.AppService appService() {
        if (!wechatPayConfig.isEnabled()) {
            log.info("微信支付已禁用，跳过初始化appService");
            return null;
        }

        if (StringUtils.isBlank(wechatPayConfig.getAppId()) ||
                StringUtils.isBlank(wechatPayConfig.getMchId()) ||
                StringUtils.isBlank(wechatPayConfig.getApiV3Key())) {
            log.warn("微信支付必要配置缺失，跳过初始化appService");
            return null;
        }

        try {
            com.wechat.pay.java.core.Config config = createConfig();
            if (config == null) {
                return null;
            }

            return new com.wechat.pay.java.service.payments.app.AppService.Builder()
                    .config(config)
                    .build();
        } catch (Exception e) {
            log.error("初始化微信支付AppService失败，跳过", e);
            return null;
        }
    }

    /**
     * H5支付服务
     */
    @Bean
    public com.wechat.pay.java.service.payments.h5.H5Service h5Service() {
        if (!wechatPayConfig.isEnabled()) {
            log.info("微信支付已禁用，跳过初始化h5Service");
            return null;
        }

        if (StringUtils.isBlank(wechatPayConfig.getAppId()) ||
                StringUtils.isBlank(wechatPayConfig.getMchId()) ||
                StringUtils.isBlank(wechatPayConfig.getApiV3Key())) {
            log.warn("微信支付必要配置缺失，跳过初始化h5Service");
            return null;
        }

        try {
            com.wechat.pay.java.core.Config config = createConfig();
            if (config == null) {
                return null;
            }

            return new com.wechat.pay.java.service.payments.h5.H5Service.Builder()
                    .config(config)
                    .build();
        } catch (Exception e) {
            log.error("初始化微信支付H5Service失败，跳过", e);
            return null;
        }
    }

    /**
     * 退款服务
     */
    @Bean
    public com.wechat.pay.java.service.refund.RefundService refundService() {
        if (!wechatPayConfig.isEnabled()) {
            log.info("微信支付已禁用，跳过初始化refundService");
            return null;
        }

        if (StringUtils.isBlank(wechatPayConfig.getAppId()) ||
                StringUtils.isBlank(wechatPayConfig.getMchId()) ||
                StringUtils.isBlank(wechatPayConfig.getApiV3Key())) {
            log.warn("微信支付必要配置缺失，跳过初始化refundService");
            return null;
        }

        try {
            com.wechat.pay.java.core.Config config = createConfig();
            if (config == null) {
                return null;
            }

            return new com.wechat.pay.java.service.refund.RefundService.Builder()
                    .config(config)
                    .build();
        } catch (Exception e) {
            log.error("初始化微信支付RefundService失败，跳过", e);
            return null;
        }
    }

    /**
     * 创建通用配置
     */
    private com.wechat.pay.java.core.Config createConfig() {
        try {
            // 1. 商户私钥
            PrivateKey merchantPrivateKey = loadPrivateKey();

            if (merchantPrivateKey == null) {
                log.warn("加载商户私钥失败");
                return null;
            }

            // 2. 商户证书序列号
            String merchantSerialNumber = wechatPayConfig.getMchSerialNo();

            if (StringUtils.isBlank(merchantSerialNumber)) {
                log.info("商户证书序列号为空，使用默认值");
                merchantSerialNumber = "";
            }

            // 3. 构建RSA配置
            com.wechat.pay.java.core.RSAAutoCertificateConfig config =
                    new com.wechat.pay.java.core.RSAAutoCertificateConfig.Builder()
                            .merchantId(wechatPayConfig.getMchId())
                            .privateKey(merchantPrivateKey)
                            .merchantSerialNumber(merchantSerialNumber)
                            .apiV3Key(wechatPayConfig.getApiV3Key())
                            .build();

            return config;
        } catch (Exception e) {
            log.error("创建微信支付配置失败", e);
            return null;
        }
    }

    /**
     * 加载商户私钥
     */
    private PrivateKey loadPrivateKey() {
        try {
            String privateKey = wechatPayConfig.getPrivateKey();

            if (StringUtils.isNotBlank(wechatPayConfig.getPrivateKeyPath())) {
                // 从文件加载
                String filePath = wechatPayConfig.getPrivateKeyPath();
                log.info("从文件加载私钥: {}", filePath);

                try {
                    privateKey = FileUtil.readString(filePath, StandardCharsets.UTF_8);
                } catch (Exception e) {
                    log.error("读取私钥文件失败: {}", filePath, e);
                }
            }

            // 检查私钥是否为空
            if (StringUtils.isBlank(privateKey)) {
                log.warn("私钥内容为空");
                return null;
            }

            // 记录私钥长度用于调试
            log.info("私钥原始长度: {}", privateKey.length());

            // 清理PEM格式
            String cleanedKey = privateKey
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");

            log.info("清理后私钥长度: {}", cleanedKey.length());

            if (StringUtils.isBlank(cleanedKey)) {
                log.warn("清理后的私钥为空");
                return null;
            }

            // Base64解码
            byte[] keyBytes = Base64.getDecoder().decode(cleanedKey);
            log.info("解码后的密钥字节长度: {}", keyBytes.length);

            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(spec);

        } catch (Exception e) {
            log.error("加载商户私钥失败", e);
            return null;
        }
    }
}
