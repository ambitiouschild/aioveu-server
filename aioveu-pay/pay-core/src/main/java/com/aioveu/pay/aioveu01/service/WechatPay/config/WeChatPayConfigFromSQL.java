package com.aioveu.pay.aioveu01.service.WechatPay.config;

import com.aioveu.pay.aioveu03PayConfigWechat.model.entity.PayConfigWechat;
import com.aioveu.pay.aioveu03PayConfigWechat.service.PayConfigWechatService;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.ehcache.impl.internal.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: WeChatPayConfigFromSQL
 * @Description TODO 微信支付配置类 - 改为从数据库获取配置
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 18:17
 * @Version 1.0
 **/
@Slf4j
//@Configuration
@Data
public class WeChatPayConfigFromSQL {

    @Autowired
    private PayConfigWechatService payConfigWechatService;

    // 缓存数据库中的配置
    private final Map<String, PayConfigWechat> configCache = new ConcurrentHashMap<>();

    /**
     * 是否启用微信支付
     */
    private boolean enabled = false;

    /**
     * 当前使用的配置
     */
    private PayConfigWechat currentConfig;

    // 配置是否已加载
    private volatile boolean initialized = false;

    // 初始化锁
    private final Object initLock = new Object();

    /**
     *   TODO 初始化配置
     *          问题根源
     *               1.@PostConstruct在应用启动时执行，此时还没有HTTP请求
     *               2.租户ID通常从请求头或Token中获取，启动时没有请求
     *               3.多租户插件在初始化时执行查询，租户ID为null
     *               在初始化时（@PostConstruct），让多租户插件忽略pay_config_wechat表的租户过滤，这样就能查询到所有租户的配置。
     */
    @PostConstruct
    public void init() {
        try {
            loadConfigFromDatabase();
            log.info("【微信支付配置】初始化完成，从数据库加载配置");
        } catch (Exception e) {
            log.error("【微信支付配置】从数据库加载配置失败", e);
        }
    }



    /**
     * 从数据库加载配置
     */
    public synchronized void loadConfigFromDatabase() {
        try {
            // 清空缓存
            configCache.clear();

            // 查询所有启用的微信支付配置
            List<PayConfigWechat> configs = payConfigWechatService.listEnabledConfigs();

            log.info("【WeChatPayConfigFromSQL】查询所有启用的微信支付配置：{}",configs);

            if (CollectionUtils.isEmpty(configs)) {
                log.warn("【微信支付配置】数据库中没有启用的微信支付配置");
                enabled = false;
                return;
            }

            // 缓存所有配置
            for (PayConfigWechat config : configs) {
                String cacheKey = buildCacheKey(config);
                configCache.put(cacheKey, config);
                log.info("【微信支付配置】加载配置: 租户ID={}, 应用ID={}, 商户号={}",
                        config.getTenantId(), config.getAppId(), config.getMchId());
            }

            // 设置默认配置（可以按业务规则选择，比如第一个或标记为默认的）
            currentConfig = configs.get(0);

//            currentConfig = configs.getConfig(0);
            enabled = true;

            log.info("【微信支付配置】从数据库加载配置成功，共{}个配置", configs.size());

        } catch (Exception e) {
            log.error("【微信支付配置】从数据库加载配置失败", e);
            enabled = false;
        }
    }

    /**
     * 根据租户和应用获取配置
     */
    public PayConfigWechat getConfig(Long tenantId, String appId) {
        String cacheKey = tenantId + ":" + appId;
        return configCache.get(cacheKey);
    }

    /**
     * 获取默认配置
     */
    public PayConfigWechat getDefaultConfig() {
        return currentConfig;
    }

    /**
     * 刷新配置缓存
     */
    public void refreshConfig() {
        loadConfigFromDatabase();
    }

    /**
     * 构建缓存key
     */
    private String buildCacheKey(PayConfigWechat config) {
        return config.getTenantId() + ":" + config.getAppId();
    }

    // ========== 原有方法的适配 ==========
    // 以下方法保持原有接口不变，但实现改为从数据库获取

    public String getAppId() {
        return currentConfig != null ? currentConfig.getAppId() : null;
    }

    public String getMchId() {
        return currentConfig != null ? currentConfig.getMchId() : null;
    }

    public String getMchKey() {
        return currentConfig != null ? currentConfig.getMchKey() : null;
    }

    public String getApiV3Key() {
        return currentConfig != null ? currentConfig.getApiV3Key() : null;
    }

    public String getMchSerialNo() {
        return currentConfig != null ? currentConfig.getMchSerialNo() : null;
    }

    public String getPrivateKey() {
        return currentConfig != null ? currentConfig.getPrivateKey() : null;
    }

    public String getPrivateKeyPath() {
        return currentConfig != null ? currentConfig.getPrivateKeyPath() : null;
    }

    public String getWechatpayPublicKeyId() {
        return currentConfig != null ? currentConfig.getWechatpayPublicKeyId() : null;
    }

    public String getWechatpayPublicKey() {
        return currentConfig != null ? currentConfig.getWechatpayPublicKey() : null;
    }

    public String getWechatpayPublicKeyPath() {
        return currentConfig != null ? currentConfig.getWechatpayPublicKeyPath() : null;
    }

    public String getPlatformCertSerialNo() {
        return currentConfig != null ? currentConfig.getPlatformCertSerialNo() : null;
    }

    public String getPlatformCertPath() {
        return currentConfig != null ? currentConfig.getPlatformCertPath() : null;
    }

//    public String getCertPath() {
//        return currentConfig != null ? currentConfig.getCertPath() : null;
//    }

//    public String getCertPassword() {
//        return currentConfig != null ? currentConfig.getCertPassword() : null;
//    }

    public String getApiDomain() {
        return currentConfig != null && StringUtils.isNotBlank(currentConfig.getApiDomain())
                ? currentConfig.getApiDomain()
                : "https://api.mch.weixin.qq.com";
    }

    public String getNotifyUrl() {
        return currentConfig != null ? currentConfig.getNotifyUrl() : null;
    }

    public String getRefundNotifyUrl() {
        return currentConfig != null ? currentConfig.getRefundNotifyUrl() : null;
    }

    public Integer isSandbox() {
        return currentConfig != null && currentConfig.getSandbox() != null
                ? currentConfig.getSandbox()
                : 0;
    }

    public String getSignType() {
        return currentConfig != null && StringUtils.isNotBlank(currentConfig.getSignType())
                ? currentConfig.getSignType()
                : "RSA";
    }

    public CertStoreType getCertStoreType() {
        if (currentConfig == null || StringUtils.isBlank(currentConfig.getCertStoreType())) {
            return CertStoreType.FILE;
        }
        return CertStoreType.valueOf(currentConfig.getCertStoreType());
    }

    public int getConnectTimeout() {
        return currentConfig != null && currentConfig.getConnectTimeout() != null
                ? currentConfig.getConnectTimeout()
                : 10;
    }

    public int getReadTimeout() {
        return currentConfig != null && currentConfig.getReadTimeout() != null
                ? currentConfig.getReadTimeout()
                : 10;
    }

//    public String getProxyHost() {
//        return currentConfig != null ? currentConfig.getProxyHost() : null;
//    }

//    public Integer getProxyPort() {
//        return currentConfig != null ? currentConfig.getProxyPort() : null;
//    }

    public Integer isAutoDownloadCert() {
        return currentConfig != null && currentConfig.getAutoDownloadCert() != null
                ? currentConfig.getAutoDownloadCert()
                : 1;
    }

    public Map<String, PaymentConfig> getPaymentMethods() {
        // 这里可以根据业务需求从数据库加载支付方式配置
        Map<String, PaymentConfig> paymentMethods = new HashMap<>();
        if (currentConfig != null) {
            // 可以根据数据库中的配置构建paymentMethods
            // 例如：paymentMethods.put("JSAPI", new PaymentConfig());
        }
        return paymentMethods;
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
     * 支付方式配置类
     */
    @Data
    public static class PaymentConfig {
        private String appId;
        private String mchId;
        private boolean enabled = true;
    }

    /**
     * 转换为微信支付SDK需要的Config
     */
    public com.wechat.pay.java.core.Config toSdkConfig() {
        try {
            if (currentConfig == null) {
                throw new RuntimeException("微信支付配置未初始化");
            }

            log.info("【微信支付】创建SDK配置，商户号: {}, 应用ID: {}, APIv3 密钥: {}, 商户证书序列号: {}",
                    getMchId(), getAppId(), getApiV3Key(), getMchSerialNo());

            // 验证必要参数
            if (StringUtils.isEmpty(getMchSerialNo())) {
                throw new IllegalArgumentException("商户证书序列号(mch-serial-no)不能为空");
            }

            String privateKey = getPrivateKey();
            if (StringUtils.isBlank(privateKey)) {
                throw new IllegalArgumentException("商户私钥不能为空");
            }

            log.debug("【微信支付】私钥内容（前100字符）: {}",
                    privateKey.substring(0, Math.min(100, privateKey.length())));

            // 验证私钥格式
            validatePrivateKey(privateKey);

            // 创建配置
            return new com.wechat.pay.java.core.RSAPublicKeyConfig.Builder()
                    .merchantId(getMchId())     // 商户号
                    .privateKey(privateKey)   // 商户私钥
                    .publicKeyId(getWechatpayPublicKeyId())          // 公钥ID
                    .publicKey(getWechatpayPublicKey())    // 公钥
                    .merchantSerialNumber(getMchSerialNo())  // 商户证书序列号
                    .apiV3Key(getApiV3Key())   // apiV3Key
                    .build();

        } catch (Exception e) {
            log.error("【微信支付】创建SDK配置失败", e);
            throw new RuntimeException("创建微信支付SDK配置失败", e);
        }
    }

    /**
     * 验证配置完整性
     */
    public void validateConfig() {
        List<String> errors = new ArrayList<>();

        if (currentConfig == null) {
            errors.add("微信支付配置未初始化");
        } else {
            if (StringUtils.isBlank(currentConfig.getMchId())) {
                errors.add("商户号(mchId)不能为空");
            }
            if (StringUtils.isBlank(currentConfig.getAppId())) {
                errors.add("应用ID(appId)不能为空");
            }
            if (StringUtils.isBlank(currentConfig.getApiV3Key())) {
                errors.add("APIv3密钥(apiV3Key)不能为空");
            }
            if (StringUtils.isBlank(currentConfig.getMchSerialNo())) {
                errors.add("商户证书序列号(mchSerialNo)不能为空");
            }
            if (StringUtils.isBlank(currentConfig.getPrivateKey())) {
                errors.add("私钥(privateKey)不能为空");
            }

            // 检查微信支付公钥
            if (StringUtils.isBlank(currentConfig.getWechatpayPublicKeyId())) {
                log.warn("微信支付公钥ID未配置，将使用平台证书模式");
            }
        }

        if (!errors.isEmpty()) {
            String errorMsg = "微信支付配置错误:\n" + String.join("\n", errors);
            log.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        log.info("✅ 微信支付配置验证通过");
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
