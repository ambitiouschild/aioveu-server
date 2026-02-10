package com.aioveu.pay.aioveuModule.service.WechatPay.requestFactory;

import com.aioveu.pay.aioveuModule.service.WechatPay.config.WeChatPayConfig;
import com.aioveu.pay.aioveuModule.model.vo.PaymentRequestDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @ClassName: WechatPayRequestFactory
 * @Description TODO 微信支付请求工厂   使用工厂模式（最推荐）
 *                  使用工厂模式解决不同支付方式实体类名相同的问题
 *                  工厂模式的优势
 *                      解决命名冲突：每种支付方式使用完整的类名，避免混淆
 *                      代码复用：公共参数设置逻辑集中在工厂中
 *                      易于维护：新增支付方式只需在工厂中添加方法
 *                      职责分离：支付服务专注于业务逻辑，工厂专注于请求构建
 *                      这样您的代码结构会更加清晰，也更容易维护和扩展。
 *
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 22:28
 * @Version 1.0
 **/

// 支付请求工厂
@Component
public class WeChatPayRequestFactory {


    private static final int AMOUNT_MULTIPLIER = 100;

    /**
     * 创建JSAPI支付请求  构建JSAPI预支付请求
     */
    public com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest createJsapiRequest(
            PaymentRequestDTO request, WeChatPayConfig config) {
        com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest prepayRequest =
                new com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest();

        // 设置金额
        com.wechat.pay.java.service.payments.jsapi.model.Amount amount =
                new com.wechat.pay.java.service.payments.jsapi.model.Amount();
        amount.setTotal(convertAmountToFen(request.getAmount()));
        prepayRequest.setAmount(amount);

        // 设置支付者（JSAPI特有）
        com.wechat.pay.java.service.payments.jsapi.model.Payer payer =
                new com.wechat.pay.java.service.payments.jsapi.model.Payer();
        payer.setOpenid(request.getOpenid());
        prepayRequest.setPayer(payer);

        // 设置公共参数
        setCommonParams(prepayRequest, request, config);
        return prepayRequest;
    }

    /**
     * 创建APP支付请求   构建App预支付请求
     */
    public com.wechat.pay.java.service.payments.app.model.PrepayRequest createAppRequest(
            PaymentRequestDTO request, WeChatPayConfig config) {
        com.wechat.pay.java.service.payments.app.model.PrepayRequest prepayRequest =
                new com.wechat.pay.java.service.payments.app.model.PrepayRequest();

        // 设置金额
        com.wechat.pay.java.service.payments.app.model.Amount amount =
                new com.wechat.pay.java.service.payments.app.model.Amount();
        amount.setTotal(convertAmountToFen(request.getAmount()));
        prepayRequest.setAmount(amount);

        // APP支付不需要设置payer
        setCommonParams(prepayRequest, request, config);
        return prepayRequest;
    }

    /**
     * 创建Native支付请求  构建Native预支付请求
     */
    public com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest createNativeRequest(
            PaymentRequestDTO request, WeChatPayConfig config) {
        com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest prepayRequest =
                new com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest();

        // 设置金额
        com.wechat.pay.java.service.payments.nativepay.model.Amount amount =
                new com.wechat.pay.java.service.payments.nativepay.model.Amount();
        amount.setTotal(convertAmountToFen(request.getAmount()));
        prepayRequest.setAmount(amount);

        // Native支付不需要设置payer
        setCommonParams(prepayRequest, request, config);
        return prepayRequest;
    }

    /**
     * 创建H5支付请求  构建H5预支付请求
     */
    public com.wechat.pay.java.service.payments.h5.model.PrepayRequest createH5Request(
            PaymentRequestDTO request, WeChatPayConfig config) {
        com.wechat.pay.java.service.payments.h5.model.PrepayRequest prepayRequest =
                new com.wechat.pay.java.service.payments.h5.model.PrepayRequest();

        // 设置金额
        com.wechat.pay.java.service.payments.h5.model.Amount amount =
                new com.wechat.pay.java.service.payments.h5.model.Amount();
        amount.setTotal(convertAmountToFen(request.getAmount()));
        prepayRequest.setAmount(amount);

        // 设置场景信息（H5特有）
        com.wechat.pay.java.service.payments.h5.model.SceneInfo sceneInfo =
                new com.wechat.pay.java.service.payments.h5.model.SceneInfo();
        sceneInfo.setPayerClientIp(request.getClientIp());

        com.wechat.pay.java.service.payments.h5.model.H5Info h5Info =
                new com.wechat.pay.java.service.payments.h5.model.H5Info();
        h5Info.setType("Wap");
        sceneInfo.setH5Info(h5Info);
        prepayRequest.setSceneInfo(sceneInfo);

        // 设置公共参数
        setCommonParams(prepayRequest, request, config);
        return prepayRequest;
    }

    /**
     * 设置公共参数
     */
    private void setCommonParams(Object prepayRequest, PaymentRequestDTO request, WeChatPayConfig config) {
        try {
            // 使用反射设置公共参数
            Class<?> requestClass = prepayRequest.getClass();

            // 设置appid
            requestClass.getMethod("setAppid", String.class).invoke(prepayRequest, config.getAppId());

            // 设置mchid
            requestClass.getMethod("setMchid", String.class).invoke(prepayRequest, config.getMchId());

            // 设置描述
            requestClass.getMethod("setDescription", String.class).invoke(prepayRequest, request.getSubject());

            // 设置通知地址
            requestClass.getMethod("setNotifyUrl", String.class).invoke(prepayRequest, config.getNotifyUrl());

            // 设置商户订单号
            requestClass.getMethod("setOutTradeNo", String.class).invoke(prepayRequest, request.getOrderNo());

        } catch (Exception e) {
            throw new RuntimeException("设置支付请求公共参数失败", e);
        }
    }

    /**
     * 金额转换：元转分
     */
    private int convertAmountToFen(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(AMOUNT_MULTIPLIER)).intValue();
    }

}
