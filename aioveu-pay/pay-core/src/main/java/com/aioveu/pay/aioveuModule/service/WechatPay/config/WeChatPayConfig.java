package com.aioveu.pay.aioveuModule.service.WechatPay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: WechatPayConfig
 * @Description TODO 微信支付配置类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 18:35
 * @Version 1.0
 **/

@Configuration
@ConfigurationProperties(prefix = "wechat.pay")
@Data
public class WeChatPayConfig {

    /**
     * 应用ID（公众号/小程序/企业微信）
     */
    private String appId;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 商户API密钥V2
     */
    private String mchKey;

    /**
     * 商户API密钥V3
     */
    private String apiV3Key;

    /**
     * 商户证书序列号
     */
    private String mchSerialNo;

    /**
     * 商户私钥路径
     */
    private String privateKeyPath;

    /**
     * 商户私钥内容
     */
    private String privateKey;

    /**
     * 平台证书序列号
     */
    private String platformCertSerialNo;

    /**
     * 平台证书路径
     */
    private String platformCertPath;

    /**
     * API证书路径
     */
    private String certPath;

    /**
     * API证书密码
     */
    private String certPassword;

    /**
     * 网关地址
     * 国内：https://api.mch.weixin.qq.com
     * 沙箱：https://api.mch.weixin.qq.com/sandboxnew
     */
    private String apiDomain = "https://api.mch.weixin.qq.com";

    /**
     * 异步通知地址
     */
    private String notifyUrl;

    /**
     * 退款通知地址
     */
    private String refundNotifyUrl;

    /**
     * 是否沙箱环境
     */
    private boolean sandbox = false;

    /**
     * 签名类型
     */
    private String signType = "RSA";

    /**
     * 证书存储方式
     */
    private CertStoreType certStoreType = CertStoreType.FILE;

    /**
     * 连接超时时间（秒）
     */
    private int connectTimeout = 10;

    /**
     * 读取超时时间（秒）
     */
    private int readTimeout = 10;

    /**
     * 代理主机
     */
    private String proxyHost;

    /**
     * 代理端口
     */
    private Integer proxyPort;

    /**
     * 是否自动下载平台证书
     */
    private boolean autoDownloadCert = true;

    /**
     * 支付方式配置
     */
    private Map<String, PaymentConfig> paymentMethods = new HashMap<>();

    /**
     * 支付方式配置类
     */
    @Data
    public static class PaymentConfig {
        private String appId;
        private String mchId;
        private boolean enabled = true;
    }

    /**
     * 证书存储方式
     */
    public enum CertStoreType {
        FILE,   // 文件存储
        STRING, // 字符串存储
        CLOUD   // 云存储
    }
}
