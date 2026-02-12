package com.aioveu.pay.aioveuModule.service.WechatPay.utils.weChatPay;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.aioveu.pay.aioveuModule.service.WechatPay.config.WeChatPayConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static com.alipay.api.AlipayConstants.SIGN_TYPE_RSA;

/**
 * @ClassName: aioveuPaySign
 * @Description TODO  生成签名工具类
 *                 依赖注入模式（推荐）
 *                  方法抽取到另一个类，这是典型的职责分离。
 *                  这是"抽取"但不是"复用"
 *                  2.1 职责分离（SRP - Single Responsibility Principle）
 *                  2.2 辅助类/工具类模式
 *                  4. 什么时候该抽取？
 *                      该抽取的情况：
 *                      1.一个类超过300行（Spring建议）
 *                      2.一个方法超过50行（可读性差）
 *                      3.逻辑可以独立（签名、验证、构建参数）
 *                      4.可能被多处使用（工具方法）
 *                      5.测试需要隔离（便于单元测试）
 *                      不该抽取的情况：
 *                      1.逻辑紧密相关（拆分后更难理解）
 *                      2.只被一个地方调用（过度设计）
 *                      3.拆分后需要大量传参（方法签名变复杂）
 *                      4.简单的一行代码（没必要抽取）
 *                      6. 这样做的好处和坏处
 *                          好处：
 *                          1.符合单一职责原则：每个类只做一件事
 *                          2.便于单元测试：可以单独测试验证器、签名器等
 *                          3.代码复用：验证器可以被其他支付方式使用
 *                          4.团队协作：不同人负责不同组件
 *                          5.维护性好：修改签名逻辑不影响其他代码
 *                          坏处：
 *                          1.类数量爆炸：从1个类变成7个类
 *                          2.依赖复杂：需要注入多个组件
 *                          3.跳转阅读：跟踪代码需要在不同类间跳转
 *                          4.过度设计：简单项目没必要这么复杂
 *                          5.性能开销：多个Bean的创建和注入
 *                     7. 折中方案
 *                     // 保持主Service，只抽取工具类
 *                     我的建议：
 *                      先按功能抽取工具类（签名、参数构建、状态转换）
 *                      保持主Service的业务流程
 *                      等类超过300行再考虑拆分
 *                      优先保证代码可读性
 *                      简单说：你现在的做法（把一些方法放到另一个页面调用）是合理的职责分离，既不是过度设计，也不是代码重复，而是良好的架构设计。
 *                      ✅ 符合KISS原则（Keep It Simple, Stupid）
 *                      有时候最好的架构就是没有过度设计的架构。
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/12 17:59
 * @Version 1.0
 **/

@Component
@Slf4j
public class aioveuWeChatPayGeneratePayParamsUtil {

    @Autowired
    private  WeChatPayConfig weChatPayConfig;

    @Autowired
    private Environment environment;

    // 缓存私钥
    private volatile PrivateKey privateKey;


    // Mock配置（硬编码，简单）
    private static final String MOCK_PRIVATE_KEY = "MIIEvQ..."; // 测试私钥
    private static final String MOCK_SIGN_PREFIX = "MOCK_SIGN_";

    /**
     * 生成签名 （统一入口）
     */
    public String sign(String message) throws Exception {
        // 1. Mock环境判断
        if (isMockEnvironment()) {
            log.info("【Mock】生成模拟签名");
            return generateMockSign(message);
        }

        // 2. 开发环境判断
        if (isDevEnvironment() && !hasRealPrivateKey()) {
            log.warn("【开发环境】使用测试私钥");
            return generateTestSign(message);
        }

        // 3. 生产环境
        log.debug("【生产环境】生成真实签名");
        return generateRealSign(message);
    }

    /**
     * 判断Mock环境（简单实现）
     */
    private boolean isMockEnvironment() {
        // 1. 系统属性
        String systemMock = System.getProperty("wechat.mock");
        if ("true".equals(systemMock)) {
            return true;
        }

        // 2. 环境变量
        String envMock = System.getenv("WECHAT_MOCK");
        if ("true".equals(envMock)) {
            return true;
        }

        // 3. 配置属性
        String configMock = environment.getProperty("pay.mock.enabled");
        if ("true".equals(configMock)) {
            return true;
        }

        return false;
    }

    /**
     * 判断开发环境
     */
    private boolean isDevEnvironment() {
        String[] profiles = environment.getActiveProfiles();
        for (String profile : profiles) {
            if ("dev".equals(profile) || "test".equals(profile)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查是否有真实私钥
     */
    private boolean hasRealPrivateKey() {
        return StringUtils.isNotBlank(weChatPayConfig.getPrivateKey()) ||
                StringUtils.isNotBlank(weChatPayConfig.getPrivateKeyPath());
    }

    /**
     * 生成Mock签名
     */
    private String generateMockSign(String message) {
        // 简单的Mock签名逻辑
        String shortMsg = message.length() > 20 ?
                message.substring(0, 20) : message;
        String base64 = Base64.getEncoder().encodeToString(shortMsg.getBytes());
        return MOCK_SIGN_PREFIX + base64.substring(0, Math.min(50, base64.length()));
    }

    /**
     * 生成测试签名
     */
    private String generateTestSign(String message) throws Exception {
        // 使用固定的测试私钥
        PrivateKey testKey = loadTestPrivateKey();
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(testKey);
        signature.update(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(signature.sign());
    }

    /**
     * 生成真实签名
     */
    private String generateRealSign(String message) throws Exception {
        PrivateKey privateKey = getPrivateKey();
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(signature.sign());
    }

    /**
     * 加载测试私钥
     */
    private PrivateKey loadTestPrivateKey() throws Exception {
        String cleanKey = MOCK_PRIVATE_KEY.replaceAll("\\s+", "");
        byte[] keyBytes = Base64.getDecoder().decode(cleanKey);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }

    /**
     * 生成JSAPI支付参数  业务逻辑不同 签名内容不同， 参数命名不同（camelCase vs lowercase）， 返回字段不同
     */
    public Map<String, Object> generateJsapiPayParams(String prepayId) throws Exception {
        long timestamp = System.currentTimeMillis() / 1000;
        String nonceStr = IdUtil.fastSimpleUUID().substring(0, 32);
        String packageStr = "prepay_id=" + prepayId;

        String signStr = String.format("%s\n%d\n%s\n%s\n",
                weChatPayConfig.getAppId(),
                timestamp,
                nonceStr,
                packageStr
        );

        String sign = sign(signStr);

        Map<String, Object> params = new HashMap<>();
        params.put("appId", weChatPayConfig.getAppId());
        params.put("timeStamp", String.valueOf(timestamp));
        params.put("nonceStr", nonceStr);
        params.put("package", packageStr);
        params.put("signType", "RSA");
        params.put("paySign", sign);

        return params;
    }

    /**
     * 模拟支付生成JSAPI支付参数
     */
    public Map<String, Object> generateJsapiPayParams() throws Exception {
        long timestamp = System.currentTimeMillis() / 1000;
        String nonceStr = IdUtil.fastSimpleUUID().substring(0, 32);
        String packageStr = "prepay_id=" + "模拟的123456";

        String signStr = String.format("%s\n%d\n%s\n%s\n",
                123456,
                timestamp,
                nonceStr,
                packageStr
        );

        String sign = sign(signStr);

        Map<String, Object> params = new HashMap<>();
        params.put("appId", weChatPayConfig.getAppId());
        params.put("timeStamp", String.valueOf(timestamp));
        params.put("nonceStr", nonceStr);
        params.put("package", packageStr);
        params.put("signType", SIGN_TYPE_RSA);
        params.put("paySign", sign);

        return params;
    }


    /**
     * 生成App支付参数
     */
    public Map<String, Object> generateAppPayParams(String prepayId) throws Exception {
        long timestamp = System.currentTimeMillis() / 1000;
        String nonceStr = IdUtil.fastSimpleUUID().substring(0, 32);

        String signStr = String.format("%s\n%d\n%s\n%s\n",
                weChatPayConfig.getAppId(),
                timestamp,
                nonceStr,
                prepayId
        );

        String sign = sign(signStr);

        Map<String, Object> params = new HashMap<>();
        params.put("appid", weChatPayConfig.getAppId());
        params.put("partnerid", weChatPayConfig.getMchId());
        params.put("prepayid", prepayId);
        params.put("package", "Sign=WXPay");
        params.put("noncestr", nonceStr);
        params.put("timestamp", String.valueOf(timestamp));
        params.put("sign", sign);

        return params;
    }

    /**
     * 获取私钥（单例+懒加载）
     */
    private PrivateKey getPrivateKey() throws Exception {
        if (privateKey == null) {
            synchronized (this) {
                if (privateKey == null) {
                    privateKey = loadPrivateKey();
                }
            }
        }
        return privateKey;
    }


    /**
     * 安全地加载私钥
     *
     */
    //问题：.replaceAll("\\s+", "")只移除了空白字符（空格、换行、制表符等），但没有移除点号.或其他非法字符。

 /*       java.lang.IllegalArgumentException: Illegal base64 character 2e
        2e是十六进制，对应字符 .（点号）。说明你的私钥内容中包含了非法字符。*/
    private PrivateKey loadPrivateKey() throws Exception {
        try {

            //临时使用测试私钥
            // 如果是开发/测试环境，使用测试私钥
            if (isDevelopmentEnvironment()) {
                log.warn("【微信支付】开发环境，使用测试私钥");
                return getTestPrivateKey();
            }

            log.info("【微信支付】开始加载私钥");

            // 1. 获取私钥内容
            String privateKeyContent = getPrivateKeyContent();
            log.debug("原始私钥长度: {}", privateKeyContent.length());

            // 2. 清理私钥内容
            String cleanKey = cleanPrivateKey(privateKeyContent);
            log.debug("清理后私钥长度: {}", cleanKey.length());

            // 3. Base64解码
            byte[] keyBytes = Base64.getDecoder().decode(cleanKey);
            log.debug("解码后字节长度: {}", keyBytes.length);

            // 4. 生成私钥
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(spec);

            log.info("【微信支付】私钥加载成功");
            return privateKey;

        } catch (Exception e) {
            log.error("【微信支付】加载私钥失败", e);
            throw new Exception("加载私钥失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取私钥内容
     */
    private String getPrivateKeyContent() throws Exception {
        // 检查是直接配置还是文件路径
        String privateKeyContent = weChatPayConfig.getPrivateKey();
        String privateKeyPath = weChatPayConfig.getPrivateKeyPath();

        if (StringUtils.isNotBlank(privateKeyContent)) {
            log.info("从配置读取私钥");
            return privateKeyContent;
        } else if (StringUtils.isNotBlank(privateKeyPath)) {
            log.info("从文件读取私钥: {}", privateKeyPath);

            if (privateKeyPath.startsWith("classpath:")) {
                // 从classpath读取
                String path = privateKeyPath.substring("classpath:".length());
                ClassPathResource resource = new ClassPathResource(path);

                if (!resource.exists()) {
                    throw new IOException("私钥文件不存在: " + path);
                }

                try (InputStream inputStream = resource.getInputStream()) {
                    byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);
                    return new String(bytes, StandardCharsets.UTF_8);
                }
            } else {
                // 从文件系统读取
                return FileUtil.readString(privateKeyPath, StandardCharsets.UTF_8);
            }
        } else {
            throw new IllegalStateException("未配置私钥，请检查wechat.pay.private-key或wechat.pay.private-key-path配置");
        }
    }

    /**
     * 清理私钥内容
     */
    private String cleanPrivateKey(String privateKeyContent) {
        if (StringUtils.isBlank(privateKeyContent)) {
            throw new IllegalArgumentException("私钥内容为空");
        }

        // 打印原始内容用于调试
        log.debug("原始私钥前100字符: {}",
                privateKeyContent.substring(0, Math.min(100, privateKeyContent.length())));

        // 1. 移除BOM
        String cleaned = privateKeyContent.replace("\uFEFF", "").replace("\uFFFE", "");

        // 2. 移除PEM头尾标记
        cleaned = cleaned.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "");

        // 3. 移除所有空白字符（包括换行、空格、制表符等）
        cleaned = cleaned.replaceAll("\\s+", "");
//你的私钥内容包含了非法字符（点号.），而你的清理逻辑只移除了空白字符，没有移除其他非法字符。
        // 4. 移除所有非Base64字符（关键修复）
        // Base64有效字符: A-Z, a-z, 0-9, +, /, =
        cleaned = cleaned.replaceAll("[^A-Za-z0-9+/=]", "");
        //这样就能确保私钥内容是纯Base64字符串，不会包含点号、中文字符、特殊符号等非法字符。

        // 5. 验证长度
        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException("清理后的私钥内容为空");
        }

        // 6. 验证Base64格式
        if (!isValidBase64(cleaned)) {
            throw new IllegalArgumentException("私钥不是有效的Base64格式");
        }

        log.debug("清理后私钥前100字符: {}",
                cleaned.substring(0, Math.min(100, cleaned.length())));

        return cleaned;
    }

    /**
     * 验证是否为有效的Base64字符串
     */
    private boolean isValidBase64(String str) {
        try {
            Base64.getDecoder().decode(str);
            return true;
        } catch (IllegalArgumentException e) {
            // 找到非法字符
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (!Character.isLetterOrDigit(c) && c != '+' && c != '/' && c != '=') {
                    log.error("发现非法Base64字符: '{}' (ASCII: {}) 在位置: {}", c, (int) c, i);
                }
            }
            return false;
        }
    }

    /**
     * 生成测试私钥
     */
    private PrivateKey getTestPrivateKey() throws Exception {
        // 这是一个生成的测试RSA私钥（仅用于开发测试）
        String testKeyBase64 =
                "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC7VJTUtDUUsxQ" +
                        "n9H9Ug0J6QzL+6C5NU6J1p6C5NU6J1p6C5NU6J1p6C5NU6J1p6C5NU6J1p6C5N" +
                        "U6J1p6C5NU6J1p6C5NU6J1p6C5NU6J1p6C5NU6J1p6C5NU6J1p6C5NU6J1p6C5" +
                        "NU6J1p6C5NU6J1p6C5NU6J1p6C5NU6J1p6C5NU6J1p6C5NU6J1p6C5NU6J1p6C" +
                        "5NU6J1p6C5NU6J1p6C5NU6J1p6C5NU6J1p6C5NU6J1p6C5NU6J1p6C5NU6J1p6" +
                        "C5NU6J1p6C5NU6J1p6C5NU6J1p6C5NU6J1p6C5NU6J1p6C5NU6J1p6C5NU6J1p";

        String cleanKey = testKeyBase64.replaceAll("\\s+", "");
        byte[] keyBytes = Base64.getDecoder().decode(cleanKey);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }

    private boolean isDevelopmentEnvironment() {
        // 判断是否为开发环境
        String profile = System.getProperty("spring.profiles.active");
        return "dev".equals(profile) || "test".equals(profile);
    }


}
