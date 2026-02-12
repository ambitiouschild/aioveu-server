package com.aioveu.pay.aioveuModule.service.WechatPay.utils.aliPay;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aioveu.pay.aioveuModule.service.AliPay.config.AlipayConfig;
import com.aioveu.pay.aioveuModule.service.WechatPay.config.WeChatPayConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: aioveuAlipayUtil
 * @Description TODO  支付宝支付参数生成工具类 专门用于生成支付宝支付所需的前端参数
 *                      支付宝不需要像微信那样生成签名参数，支付宝返回的就是一个可以直接使用的支付参数字符串。
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/12 18:29
 * @Version 1.0
 **/

@Component
@Slf4j
public class aioveuAlipayGeneratePayParamsUtil {


    @Autowired
    private AlipayConfig alipayConfig;

    /**
     * 生成APP支付参数 - 从支付宝返回的orderString生成
     *
     * @param orderString 支付宝返回的orderString
     * @return 前端可用的支付参数
     */
    public Map<String, Object> generateAppPayParams(String orderString) throws Exception {
        try {
            log.info("【支付宝】生成APP支付参数，orderString长度: {}",
                    orderString != null ? orderString.length() : 0);

            if (orderString == null || orderString.trim().isEmpty()) {
                throw new IllegalArgumentException("支付宝orderString不能为空");
            }

            // 支付宝返回的orderString可以直接用于前端调用
            // 但为了统一接口，我们包装成结构化的参数
            Map<String, Object> params = new HashMap<>();

            // 支付宝APP支付需要以下参数
            params.put("orderString", orderString);  // 最重要的参数
            params.put("appId", alipayConfig.getAppId());
            params.put("timestamp", System.currentTimeMillis() / 1000);
            params.put("nonceStr", generateNonceStr());
            params.put("signType", alipayConfig.getSignType());

            // 解析orderString，提取一些信息（可选）
            try {
                Map<String, String> parsedParams = parseOrderString(orderString);
                if (parsedParams.containsKey("out_trade_no")) {
                    params.put("orderNo", parsedParams.get("out_trade_no"));
                }
                if (parsedParams.containsKey("total_amount")) {
                    params.put("amount", parsedParams.get("total_amount"));
                }
                if (parsedParams.containsKey("subject")) {
                    params.put("subject", parsedParams.get("subject"));
                }
            } catch (Exception e) {
                log.warn("解析orderString失败，不影响支付", e);
            }

            log.info("【支付宝】APP支付参数生成成功，参数数量: {}", params.size());
            return params;

        } catch (Exception e) {
            log.error("【支付宝】生成APP支付参数失败", e);
            throw new RuntimeException("生成支付宝APP支付参数失败: " + e.getMessage(), e);
        }
    }

    /**
     * 生成简化版支付参数
     * 返回前端可以直接使用的格式
     */
    public Map<String, Object> generateSimplePayParams(String orderString) {
        try {
            Map<String, Object> params = new HashMap<>();

            // 支付宝APP支付只需要orderString
            params.put("orderString", orderString);
            params.put("appId", alipayConfig.getAppId());
            params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
            params.put("nonceStr", generateNonceStr());

            return params;

        } catch (Exception e) {
            log.error("【支付宝】生成简化支付参数失败", e);
            throw new RuntimeException("生成支付宝支付参数失败", e);
        }
    }

    /**
     * 生成微信风格的支付参数（如果需要统一接口）
     */
    public Map<String, Object> generateWechatStyleParams(String orderString) {
        try {
            Map<String, Object> params = new HashMap<>();

            // 为了兼容某些前端，可以生成类似微信的格式
            params.put("appId", alipayConfig.getAppId());
            params.put("timeStamp", System.currentTimeMillis() / 1000);
            params.put("nonceStr", generateNonceStr());
            params.put("package", orderString);  // 这里放orderString
            params.put("signType", alipayConfig.getSignType());
            params.put("paySign", "");  // 支付宝不需要这个，但为了兼容性可以留空

            return params;

        } catch (Exception e) {
            log.error("【支付宝】生成微信风格参数失败", e);
            throw new RuntimeException("生成支付参数失败", e);
        }
    }

    /**
     * 解析orderString（可选）
     * orderString格式：app_id=xxx&biz_content=xxx&sign=xxx
     */
    private Map<String, String> parseOrderString(String orderString) {
        Map<String, String> params = new HashMap<>();

        if (orderString == null || orderString.isEmpty()) {
            return params;
        }

        try {
            // 按&分割参数
            String[] pairs = orderString.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=", 2);
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String value = keyValue[1];
                    params.put(key, value);

                    // 特别解析biz_content
                    if ("biz_content".equals(key)) {
                        parseBizContent(value, params);
                    }
                }
            }
        } catch (Exception e) {
            log.warn("解析orderString失败: {}", e.getMessage());
        }

        return params;
    }

    /**
     * 解析biz_content（JSON格式）
     */
    private void parseBizContent(String bizContent, Map<String, String> params) {
        try {
            // URL解码
            String decoded = java.net.URLDecoder.decode(bizContent, "UTF-8");

            // 解析JSON
            JSONObject json = JSONUtil.parseObj(decoded);

            // 提取重要字段
            if (json.containsKey("out_trade_no")) {
                params.put("out_trade_no", json.getStr("out_trade_no"));
            }
            if (json.containsKey("total_amount")) {
                params.put("total_amount", json.getStr("total_amount"));
            }
            if (json.containsKey("subject")) {
                params.put("subject", json.getStr("subject"));
            }
            if (json.containsKey("body")) {
                params.put("body", json.getStr("body"));
            }

        } catch (Exception e) {
            log.warn("解析biz_content失败: {}", e.getMessage());
        }
    }

    /**
     * 生成随机字符串
     */
    private String generateNonceStr() {
        return IdUtil.fastSimpleUUID().substring(0, 16);
    }

    /**
     * 验证配置
     */
    public void validateConfig() {
        if (alipayConfig == null) {
            throw new IllegalStateException("支付宝配置未注入");
        }
        if (alipayConfig.getAppId() == null || alipayConfig.getAppId().isEmpty()) {
            throw new IllegalStateException("支付宝appId未配置");
        }
//        if (alipayConfig.getPrivateKey() == null || alipayConfig.getPrivateKey().isEmpty()) {
//            throw new IllegalStateException("支付宝私钥未配置");
//        }

        log.info("【支付宝】配置验证通过，APP_ID: {}", alipayConfig.getAppId());
    }
}
