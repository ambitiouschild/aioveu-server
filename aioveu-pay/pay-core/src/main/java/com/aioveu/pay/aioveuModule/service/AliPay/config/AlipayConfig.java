package com.aioveu.pay.aioveuModule.service.AliPay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: AlipayConfig
 * @Description TODO  支付宝配置类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 17:56
 * @Version 1.0
 **/

@Configuration
@ConfigurationProperties(prefix = "alipay")
@Data
public class AlipayConfig {

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 应用私钥
     */
    private String appPrivateKey;

    /**
     * 支付宝公钥
     */
    private String alipayPublicKey;

    /**
     * 网关地址
     * 线上：https://openapi.alipay.com/gateway.do
     * 沙箱：https://openapi.alipaydev.com/gateway.do
     */
    private String serverUrl;

    /**
     * 签名算法类型
     */
    private String signType = "RSA2";

    /**
     * 数据格式
     */
    private String format = "json";

    /**
     * 字符集
     */
    private String charset = "UTF-8";

    /**
     * 是否使用证书
     */
    private boolean useCert = false;

    /**
     * 应用公钥证书路径
     */
    private String appCertPath;

    /**
     * 支付宝公钥证书路径
     */
    private String alipayCertPath;

    /**
     * 支付宝根证书路径
     */
    private String alipayRootCertPath;

    /**
     * 异步通知地址
     */
    private String notifyUrl;

    /**
     * 同步返回地址
     */
    private String returnUrl;

    /**
     * 加密方式
     */
    private String encryptType = "AES";

    /**
     * 加密密钥
     */
    private String encryptKey;

    /**
     * 超时时间（单位：秒）
     */
    private int timeout = 10;

    /**
     * 代理主机
     */
    private String proxyHost;

    /**
     * 代理端口
     */
    private Integer proxyPort;
}
