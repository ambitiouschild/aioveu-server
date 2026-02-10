package com.aioveu.pay.aioveuModule.service.WechatPay.configuration;

import cn.hutool.core.io.FileUtil;
import com.aioveu.pay.aioveuModule.service.WechatPay.config.WeChatPayConfig;
import io.micrometer.common.util.StringUtils;
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
 * @Description TODO 微信支付客户端配置
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

    /**
     * 创建微信支付V3客户端
     */
    @Bean
    public com.wechat.pay.java.service.payments.jsapi.JsapiService jsapiService()
            throws Exception {
        // 配置
        com.wechat.pay.java.core.Config config = createConfig();

        // 初始化服务
        return new com.wechat.pay.java.service.payments.jsapi.JsapiService.Builder()
                .config(config)
                .build();
    }

    /**
     * 创建Native支付服务
     */
//    @Bean
//    public com.wechat.pay.java.service.payments.nativepay.NativeService nativeService()
//            throws Exception {
//        com.wechat.pay.java.core.Config config = createConfig();
//
//        return new com.wechat.pay.java.service.payments.nativepay.NativeService.Builder()
//                .config(config)
//                .build();
//    }

    /**
     * 创建App支付服务
     */
    @Bean
    public com.wechat.pay.java.service.payments.app.AppService appService()
            throws Exception {
        com.wechat.pay.java.core.Config config = createConfig();

        return new com.wechat.pay.java.service.payments.app.AppService.Builder()
                .config(config)
                .build();
    }

    /**
     * 创建H5支付服务
     */
    @Bean
    public com.wechat.pay.java.service.payments.h5.H5Service h5Service()
            throws Exception {
        com.wechat.pay.java.core.Config config = createConfig();

        return new com.wechat.pay.java.service.payments.h5.H5Service.Builder()
                .config(config)
                .build();
    }

    /**
     * 创建退款服务
     */
    @Bean
    public com.wechat.pay.java.service.refund.RefundService refundService()
            throws Exception {
        com.wechat.pay.java.core.Config config = createConfig();

        return new com.wechat.pay.java.service.refund.RefundService.Builder()
                .config(config)
                .build();
    }

    /**
     * 创建通用配置
     */
    private com.wechat.pay.java.core.Config createConfig() throws Exception {
        // 1. 商户私钥
        PrivateKey merchantPrivateKey = loadPrivateKey();

        // 2. 商户证书序列号
        String merchantSerialNumber = wechatPayConfig.getMchSerialNo();

        // 3. 获取平台证书
        X509Certificate wechatPayCertificate = loadPlatformCertificate();

        // 4. 构建RSA配置
        com.wechat.pay.java.core.RSAAutoCertificateConfig config =
                new com.wechat.pay.java.core.RSAAutoCertificateConfig.Builder()
                        .merchantId(wechatPayConfig.getMchId())
                        .privateKey(merchantPrivateKey)
                        .merchantSerialNumber(merchantSerialNumber)
                        .apiV3Key(wechatPayConfig.getApiV3Key())
                        .build();

        return config;
    }

    /**
     * 加载商户私钥
     */
    private PrivateKey loadPrivateKey() throws Exception {
        String privateKey = wechatPayConfig.getPrivateKey();

        if (StringUtils.isNotBlank(wechatPayConfig.getPrivateKeyPath())) {
            // 从文件加载
            privateKey = FileUtil.readString(
                    wechatPayConfig.getPrivateKeyPath(),
                    StandardCharsets.UTF_8
            );
        }

        // 清理PEM格式
        privateKey = privateKey
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }

    /**
     * 加载平台证书
     */
    private X509Certificate loadPlatformCertificate() throws Exception {
        if (StringUtils.isNotBlank(wechatPayConfig.getPlatformCertPath())) {
            // 从文件加载
            String certContent = FileUtil.readString(
                    wechatPayConfig.getPlatformCertPath(),
                    StandardCharsets.UTF_8
            );

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream is = new ByteArrayInputStream(certContent.getBytes());
            return (X509Certificate) cf.generateCertificate(is);
        }

        return null;
    }

    /**
     * 创建V2版本客户端（兼容旧版）
     */
//    @Bean
//    public WXPay wxPayV2() throws Exception {
//        WXPayConfig config = new WXPayConfig() {
//            @Override
//            public String getAppID() {
//                return wechatPayConfig.getAppId();
//            }
//
//            @Override
//            public String getMchID() {
//                return wechatPayConfig.getMchId();
//            }
//
//            @Override
//            public String getKey() {
//                return wechatPayConfig.getMchKey();
//            }
//
//            @Override
//            public InputStream getCertStream() throws IOException {
//                if (StringUtils.isNotBlank(wechatPayConfig.getCertPath())) {
//                    return new FileInputStream(wechatPayConfig.getCertPath());
//                }
//                return null;
//            }
//
//            @Override
//            public int getHttpConnectTimeoutMs() {
//                return wechatPayConfig.getConnectTimeout() * 1000;
//            }
//
//            @Override
//            public int getHttpReadTimeoutMs() {
//                return wechatPayConfig.getReadTimeout() * 1000;
//            }
//        };
//
//        return new WXPay(config, wechatPayConfig.getNotifyUrl(),
//                wechatPayConfig.isSandbox(),
//                wechatPayConfig.isSandbox());
//    }
}
