package com.aioveu.pay.aioveuModule.service.WechatPay.utils;

import cn.hutool.http.HttpUtil;
import com.aioveu.pay.aioveuModule.service.WechatPay.config.WeChatPayConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @ClassName: WechatPayUtil
 * @Description TODO
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 20:41
 * @Version 1.0
 **/

@Component
@Slf4j
public class WeChatPayUtil {

    @Autowired
    private WeChatPayConfig wechatPayConfig;

    /**
     * 生成随机字符串
     */
    public String generateNonceStr() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }

    /**
     * 生成时间戳
     */
    public String generateTimestamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    /**
     * 生成签名（V2）
     */
    public String generateSignV2(Map<String, String> params, String signType) {
        // 1. 参数排序
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);

        // 2. 构建签名字符串
        StringBuilder signStr = new StringBuilder();
        for (String key : keys) {
            String value = params.get(key);
            if (StringUtils.isNotBlank(value) && !"sign".equals(key)) {
                if (signStr.length() > 0) {
                    signStr.append("&");
                }
                signStr.append(key).append("=").append(value);
            }
        }

        // 3. 拼接密钥
        signStr.append("&key=").append(wechatPayConfig.getMchKey());

        // 4. 生成签名
        if ("MD5".equalsIgnoreCase(signType)) {
            return DigestUtils.md5DigestAsHex(signStr.toString().getBytes()).toUpperCase();
        } else if ("HMAC-SHA256".equalsIgnoreCase(signType)) {
            return hmacSha256(signStr.toString(), wechatPayConfig.getMchKey()).toUpperCase();
        }

        throw new IllegalArgumentException("不支持的签名类型: " + signType);
    }

    /**
     * HMAC-SHA256签名
     */
    private String hmacSha256(String data, String key) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] hash = sha256_HMAC.doFinal(data.getBytes());
            return Hex.encodeHexString(hash);
        } catch (Exception e) {
            log.error("HMAC-SHA256签名失败", e);
            throw new RuntimeException("签名失败", e);
        }
    }

    /**
     * 验证签名（V2）
     */
    public boolean verifySignV2(Map<String, String> params, String sign) {
        String generatedSign = generateSignV2(params, "MD5");
        return generatedSign.equals(sign);
    }

    /**
     * 金额转换：元转分
     */
    public int yuanToFen(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(100)).intValue();
    }

    /**
     * 金额转换：分转元
     */
    public BigDecimal fenToYuan(int fen) {
        return new BigDecimal(fen).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    /**
     * 构建XML请求
     */
    public String buildXmlRequest(Map<String, String> params) {
        StringBuilder xml = new StringBuilder();
        xml.append("<xml>");

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (StringUtils.isNotBlank(value)) {
                xml.append("<").append(key).append(">");
                xml.append("<![CDATA[").append(value).append("]]>");
                xml.append("</").append(key).append(">");
            }
        }

        xml.append("</xml>");
        return xml.toString();
    }

    /**
     * 解析XML响应
     */
    public Map<String, String> parseXmlResponse(String xml) throws Exception {
        Map<String, String> result = new HashMap<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xml)));

        document.getDocumentElement().normalize();
        NodeList nodeList = document.getDocumentElement().getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                result.put(node.getNodeName(), node.getTextContent());
            }
        }

        return result;
    }

    /**
     * 获取沙箱密钥
     */
    public String getSandboxKey() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("mch_id", wechatPayConfig.getMchId());
        params.put("nonce_str", generateNonceStr());
        params.put("sign", generateSignV2(params, "MD5"));

        String xmlRequest = buildXmlRequest(params);

        // 发送请求到沙箱接口
        String response = HttpUtil.post(
                "https://api.mch.weixin.qq.com/sandboxnew/pay/getsignkey",
                xmlRequest
        );

        Map<String, String> responseMap = parseXmlResponse(response);
        return responseMap.get("sandbox_signkey");
    }
}
