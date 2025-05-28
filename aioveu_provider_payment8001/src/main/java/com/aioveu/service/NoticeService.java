package com.aioveu.service;

import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/21 0021 0:26
 */
public interface NoticeService {

    /**
     * 微信支付结果通知
     * @param xml
     * @return
     */
    String weChatPayNotice(String xml);





    String fieldWxPayNotice(String xml);

    String vipWxPayNotice(String xml);

    String productWxPayNotice(String xml);

    /**
     * 微信退款结果通知
     * @param xml
     * @return
     */
    String weChatRefundNotice(String xml);

    /**
     * 充值 微信支付结果通知
     * @param xml
     * @return
     */
    String weChatRechargePayNotice(String xml);





    /**
     * 获取服务器缓存的验证码
     * @param phone
     * @param cachePrefix
     * @return
     */
    String getCode(String phone, String cachePrefix);



    /**
     * 每周一 上课提醒
     * @return
     */
    boolean everyWeekGradeTips();

    /**
     * 用于接收平台推送给微信第三方平台账号的消息与事件，如授权事件通知、component_verify_ticket等
     * @param requestBody 加密消息
     * @param timestamp  时间戳
     * @param nonce 随机数
     * @param signature 加密
     * @param encType  加密方式
     * @param msgSignature 签名，微信服务器会验证签名
     * @return
     */
    String wxOpenReceiveTicket(String requestBody, String timestamp, String nonce, String signature, String encType, String msgSignature);


    /**
     * 微信第三方平台消息与事件接收
     * 用于代授权的公众号或小程序的接收平台推送的消息与事件
     * @param requestBody
     * @param appId
     * @param signature
     * @param timestamp
     * @param nonce
     * @param openid
     * @param encType
     * @param msgSignature
     * @return
     */
    String wxOpenCallBack(String requestBody,
                          String appId,
                          String signature,
                          String timestamp,
                          String nonce,
                          String openid,
                          String encType,
                          String msgSignature);

    /**
     * 汇付支付异步通知回调
     * @param params
     * @return
     */
    String huiFuPayCallback(Map<String, Object> params);

    /**
     * 汇付退款
     * @param params
     * @return
     */
    String huiFuRefund(Map<String, Object> params);

    /**
     * 商家充值订单支付回调
     * @param xml
     * @return
     */
    String storeRechargeNotice(String xml);
}
