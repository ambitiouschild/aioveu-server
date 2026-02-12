package com.aioveu.pay.aioveuModule.service.WechatPay.config;

import com.alipay.api.internal.util.file.FileUtils;
import com.alipay.api.internal.util.file.IOUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: WechatPayConfig
 * @Description TODO 微信支付配置类  作用：读取外部配置文件，封装配置属性
 *                      你可以直接读取配置，但需要转换：
 *                      你的配置类：WeChatPayConfig（自定义的）
 *                      微信SDK需要的配置：com.wechat.pay.java.core.Config
 *                      你需要：
 *                      在 WeChatPayConfig中添加 toSdkConfig()方法
 *                      将你的配置转换为微信 SDK 需要的格式
 *                      使用转换后的配置创建支付服务
 *                      这样既保持了配置的统一管理，又满足了微信 SDK 的要求。
 *
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 18:35
 * @Version 1.0
 **/

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "pay.wechat")
@Data
public class WeChatPayConfig {


    /**
     * 是否启用微信支付
     */
    private boolean enabled = false;  // 添加enabled字段，默认false

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

    /**
     * 转换为微信支付SDK需要的Config
     */
    public com.wechat.pay.java.core.Config toSdkConfig() {
        try {
            log.info("【微信支付】创建SDK配置，商户号: {}, 应用ID: {}", mchId, appId);

            // 1. 加载私钥
            String privateKey = loadPrivateKey();
            log.debug("【微信支付】私钥内容（前100字符）: {}",
                    privateKey.substring(0, Math.min(100, privateKey.length())));

            // 2. 验证私钥格式
            validatePrivateKey(privateKey);

            // 2. 创建配置
            return new com.wechat.pay.java.core.RSAAutoCertificateConfig.Builder()
                    .merchantId(mchId)
                    .privateKey(privateKey)
                    .merchantSerialNumber(mchSerialNo)
                    .apiV3Key(apiV3Key)
                    .build();

        } catch (Exception e) {
            log.error("【微信支付】创建SDK配置失败", e);
            throw new RuntimeException("创建微信支付SDK配置失败", e);
        }
    }

    /**
     * 验证私钥格式
     */
    private void validatePrivateKey(String privateKey) {
        if (StringUtils.isBlank(privateKey)) {
            throw new RuntimeException("私钥内容为空");
        }

        // 检查是否包含必要的标记
        if (!privateKey.contains("-----BEGIN PRIVATE KEY-----")) {
            throw new RuntimeException("私钥格式错误：缺少 BEGIN PRIVATE KEY 标记");
        }

        if (!privateKey.contains("-----END PRIVATE KEY-----")) {
            throw new RuntimeException("私钥格式错误：缺少 END PRIVATE KEY 标记");
        }

        // 检查是否是 PKCS#8 格式
        if (privateKey.contains("-----BEGIN RSA PRIVATE KEY-----")) {
            throw new RuntimeException("私钥格式错误：检测到PKCS#1格式，需要PKCS#8格式");
        }

        log.info("【微信支付】私钥格式验证通过，长度: {}", privateKey.length());
    }

    /**
     * 加载私钥  如果是从微信支付平台下载的证书： 下载的 apiclient_key.pem应该是 PKCS#8 格式 如果格式不对，用 OpenSSL 转换
     */
    private String loadPrivateKey() throws IOException {
        try {
            log.info("【微信支付】加载私钥文件: {}", privateKeyPath);

            String keyContent;
            if (privateKeyPath.startsWith("classpath:")) {
                // 从classpath加载
                String path = privateKeyPath.substring("classpath:".length());
                Resource resource = new ClassPathResource(path);

                if (!resource.exists()) {
                    throw new RuntimeException("私钥文件不存在: " + path);
                }

                keyContent = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);

            } else {
                // 从文件系统加载
                File file = new File(privateKeyPath);
                if (!file.exists()) {
                    throw new RuntimeException("私钥文件不存在: " + privateKeyPath);
                }

                keyContent = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            }

            // 清理私钥内容
            keyContent = cleanPrivateKey(keyContent);
            log.info("【微信支付】私钥加载成功，长度: {}", keyContent.length());

            return keyContent;

        } catch (Exception e) {
            log.error("【微信支付】加载私钥失败", e);
            throw new RuntimeException("加载私钥失败: " + e.getMessage(), e);
        }
    }

    /**
     * 清理私钥内容
     */
    private String cleanPrivateKey(String privateKey) {
        if (StringUtils.isBlank(privateKey)) {
            return privateKey;
        }

        // 移除可能的BOM
        privateKey = privateKey.replace("\uFEFF", "");

        // 确保正确的换行符
        privateKey = privateKey.replace("\r\n", "\n");

        // 确保头尾标记正确
        if (!privateKey.startsWith("-----BEGIN PRIVATE KEY-----")) {
            privateKey = "-----BEGIN PRIVATE KEY-----\n" + privateKey;
        }

        if (!privateKey.endsWith("-----END PRIVATE KEY-----\n")) {
            if (!privateKey.endsWith("-----END PRIVATE KEY-----")) {
                privateKey = privateKey + "\n-----END PRIVATE KEY-----";
            } else {
                privateKey = privateKey + "\n";
            }
        }

        return privateKey;
    }

}
