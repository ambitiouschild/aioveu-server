package com.aioveu.pay.aioveu00Payment.utils;

import com.aioveu.pay.aioveuModule.model.vo.PaymentCallbackDTO;
import com.aioveu.pay.aioveuModule.service.AliPay.config.AlipayConfig;
import com.aioveu.pay.aioveuModule.service.WechatPay.config.WeChatPayConfig;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * @ClassName: verifyCallbackSign
 * @Description TODO 支付回调签名验证工具类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/11 21:24
 * @Version 1.0
 **/


@Slf4j
@Component
@RequiredArgsConstructor
public class verifyCallbackSign {


    private final WeChatPayConfig weChatPayConfig;
    private final AlipayConfig alipayConfig;

    /**
     * 验证回调签名
     */
    public boolean verifyCallbackSign(PaymentCallbackDTO callback) {
        String channel = callback.getChannel();

        try {
            switch (channel) {
                case "wechat":
                    return verifyWechatSign(callback);
                case "alipay":
                    return verifyAlipaySign(callback);
                case "balance":
                    return true;  // 余额支付不需要签名验证
                default:
                    log.warn("【签名验证】未知支付渠道: {}", channel);
                    return false;
            }
        } catch (Exception e) {
            log.error("【签名验证】验证异常: {}", channel, e);
            return false;
        }
    }

    /**
     * 验证微信支付签名
     */
    private boolean verifyWechatSign(PaymentCallbackDTO callback) {
        // 获取微信支付配置

        // 构建签名字符串
        Map<String, String> params = new HashMap<>();
        params.put("appid", weChatPayConfig.getAppId());
        params.put("mch_id", weChatPayConfig.getMchId());
        params.put("nonce_str", callback.getThirdPaymentNo());
        params.put("out_trade_no", callback.getPaymentNo());
        params.put("total_fee", String.valueOf(callback.getAmount().multiply(new BigDecimal(100)).intValue()));
        params.put("result_code", callback.getSuccess() ? "SUCCESS" : "FAIL");

        // 生成签名
        String sign = WxPayUtil.generateSignature(params, weChatPayConfig.getMchKey());

        // 比较签名
        return sign.equals(callback.getSign());
    }

    /**
     * 验证支付宝签名
     */
    private boolean verifyAlipaySign(PaymentCallbackDTO callback) throws AlipayApiException {
        // 获取支付宝配置

        // 创建Alipay客户端
        AlipayClient alipayClient = new DefaultAlipayClient(
                alipayConfig.getGatewayUrl(),
                alipayConfig.getAppId(),
                alipayConfig.getPrivateKey(),
                "json",
                "utf-8",
                alipayConfig.getPublicKey(),
                "RSA2"
        );

        // 验证签名
        String content = callback.getRawData();
        String sign = callback.getSign();
        String signType = callback.getSignType();

        return alipayClient.verify(content, sign, alipayConfig.getPublicKey(),
                "utf-8", signType);
    }
}
