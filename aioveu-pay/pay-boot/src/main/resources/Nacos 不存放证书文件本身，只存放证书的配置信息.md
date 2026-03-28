**Nacos 不存放证书文件本身，只存放证书的配置信息**。

让我为您解释清楚：

## Nacos 配置与证书文件的关系

### 1. **Nacos 的角色**

- Nacos 只存储**配置文件**（YAML/Properties）
- 证书文件**不放在 Nacos 中**
- 证书文件放在**应用的 classpath** 中

### 2. **文件存储位置**

```
您的项目
├── src/main/resources/
│   ├── application.yml              # 本地配置
│   ├── bootstrap.yml                # Nacos连接配置
│   └── certs/                       # 证书文件目录
│       ├── apiclient_key.pem        # 微信商户私钥
│       ├── apiclient_cert.pem       # 微信商户证书
│       └── wechatpay_cert.pem       # 微信平台证书
└── 构建后的项目
    └── target/classes/
        ├── application.yml
        ├── bootstrap.yml
        └── certs/
            └── 所有证书文件
```

## 正确的配置流程

### 步骤1：在项目中创建证书目录

```
# 在 resources 目录下创建 certs 目录
mkdir -p src/main/resources/certs

# 从微信商户平台下载的证书放到这个目录
# 1. apiclient_key.pem    商户私钥
# 2. apiclient_cert.pem   商户证书
# 3. wechatpay_cert.pem   微信平台证书
```

### 步骤2：在 Nacos 中配置路径

在 Nacos 控制台创建配置：

**Data ID**: `wechat-pay-config.yaml`

**Group**: `AIOVEU_GROUP`

**Content**:

```
wechat:
  pay:
    enabled: true
    app-id: "wx8888888888888888"      # 您的微信AppID
    mch-id: "1900000109"              # 您的商户号
    mch-serial-no: "3775B6A45ACD588826D15E583A95F5DD********"  # 证书序列号
    api-v3-key: "APIv3密钥32位字符"    # APIv3密钥
    
    # 关键：配置证书文件路径
    private-key-path: "classpath:certs/apiclient_key.pem"
    certificate-path: "classpath:certs/apiclient_cert.pem"
    platform-cert-path: "classpath:certs/wechatpay_cert.pem"
    
    # 回调地址
    notify-url: "http://您的域名/api/payment/wechat/notify"
    refund-notify-url: "http://您的域名/api/payment/wechat/refund/notify"
    
    # 是否沙箱环境
    sandbox: false
```

### 步骤3：修改 WeChatPayConfiguration

```
package com.aioveu.pay.aioveu01.service.WechatPay.configuration;

import com.aioveu.pay.aioveu01.service.WechatPay.config.WeChatPayConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

/**
 * 微信支付配置
 */
@Configuration
@Slf4j
@ConditionalOnProperty(name = "wechat.pay.enabled", havingValue = "true", matchIfMissing = false)
public class WeChatPayConfiguration {

    @Autowired
    private WeChatPayConfig wechatPayConfig;
    
    @Autowired
    private ResourceLoader resourceLoader;  // Spring的资源加载器

    /**
     * 加载商户私钥
     */
    private PrivateKey loadPrivateKey() throws Exception {
        String privateKey = wechatPayConfig.getPrivateKey();

        // 1. 如果直接配置了私钥内容
        if (StringUtils.hasText(privateKey)) {
            log.info("使用直接配置的私钥内容");
            return parsePrivateKey(privateKey);
        }

        // 2. 从文件加载
        String filePath = wechatPayConfig.getPrivateKeyPath();
        if (StringUtils.hasText(filePath)) {
            log.info("从文件加载私钥: {}", filePath);
            return loadPrivateKeyFromFile(filePath);
        }

        throw new RuntimeException("微信支付私钥未配置");
    }
    
    /**
     * 从文件加载私钥
     */
    private PrivateKey loadPrivateKeyFromFile(String filePath) throws Exception {
        try {
            // Spring会自动处理 classpath: 前缀
            Resource resource = resourceLoader.getResource(filePath);
            
            if (!resource.exists()) {
                log.error("私钥文件不存在: {}", filePath);
                throw new RuntimeException("私钥文件不存在: " + filePath);
            }
            
            // 读取文件内容
            String privateKeyContent;
            try (InputStream is = resource.getInputStream()) {
                Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name());
                scanner.useDelimiter("\\A");
                privateKeyContent = scanner.hasNext() ? scanner.next() : "";
            }
            
            log.info("成功加载私钥文件: {}", filePath);
            return parsePrivateKey(privateKeyContent);
            
        } catch (Exception e) {
            log.error("加载私钥文件失败: {}", filePath, e);
            throw new RuntimeException("加载私钥文件失败: " + filePath, e);
        }
    }
    
    /**
     * 解析私钥字符串
     */
    private PrivateKey parsePrivateKey(String privateKeyContent) throws Exception {
        // 清理PEM格式
        String privateKey = privateKeyContent
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
        
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }
    
    // 其他方法...
}
```

## 临时解决方案（让应用先启动）

### 方案1：创建测试证书文件

```
# 在 resources 目录创建测试证书
mkdir -p src/main/resources/certs

# 创建测试私钥文件
cat > src/main/resources/certs/apiclient_key.pem << 'EOF'
-----BEGIN PRIVATE KEY-----
MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC6jV2...
-----END PRIVATE KEY-----
EOF
```

### 方案2：在 Nacos 中禁用微信支付

```
# Nacos配置
wechat:
  pay:
    enabled: false
```

### 方案3：使用临时配置

在 `bootstrap.yml`中添加覆盖配置：

```
# bootstrap.yml
wechat:
  pay:
    enabled: false
    app-id: "test"
    mch-id: "test"
    api-v3-key: "test"
```

## 完整的证书获取流程

### 1. 从微信商户平台下载证书

```
微信商户平台 -> 账户中心 -> API安全 -> 申请API证书
```

### 2. 下载的文件

```
证书文件.zip
├── apiclient_cert.pem    # 商户证书
├── apiclient_key.pem     # 商户私钥
└── 证书使用说明.txt
```

### 3. 获取平台证书

```
// 通过API获取平台证书
// 或者从 https://api.mch.weixin.qq.com/v3/certificates 下载
```

## 总结

**关键点**：

1. ✅ **Nacos 只存路径，不存文件**
2. ✅ **证书文件放在项目的 classpath 中**
3. ✅ **路径用 `classpath:`前缀**
4. ✅ **Spring 的 `ResourceLoader`会自动处理**

**您的解决方案**：

1. 创建 `src/main/resources/certs/`目录
2. 在 Nacos 配置路径
3. 使用正确的文件加载方法

**现在应用应该能启动了！** 🎉