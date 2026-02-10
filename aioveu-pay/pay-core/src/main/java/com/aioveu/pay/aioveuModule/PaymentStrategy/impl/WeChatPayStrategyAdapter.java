package com.aioveu.pay.aioveuModule.PaymentStrategy.impl;

import com.aioveu.pay.aioveuModule.PaymentStrategy.PaymentStrategy;
import com.aioveu.pay.aioveuModule.model.vo.*;
import com.aioveu.pay.aioveuModule.service.WechatPay.service.WeChatPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName: WeChatPayStrategyAdapter
 * @Description TODO  微信支付策略适配器 - 直接使用现有的WechatPayService
 *                      1.代码复用：直接使用现有的微信支付实现，无需重写
 *                      2.解耦：策略模式将支付逻辑与业务逻辑分离
 *                      3.扩展性：新增支付渠道只需添加新的策略实现
 *                      4.维护性：各支付渠道的实现相互独立，便于维护
 *                      5.这样设计既利用了您已经完成的微信支付实现，又提供了统一的策略接口，便于后续扩展其他支付方式。
 *
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 23:05
 * @Version 1.0
 **/

@Component("WeChatPayService")
public class WeChatPayStrategyAdapter implements PaymentStrategy {

    private final WeChatPayService weChatPayService;

    @Autowired
    public WeChatPayStrategyAdapter(WeChatPayService weChatPayService) {
        this.weChatPayService = weChatPayService;
    }

    @Override
    public PaymentParamsVO appPay(String paymentNo, PaymentRequestDTO request) {


        //如果现有的 WechatPayService已经支持多种支付方式（JSAPI、APP、Native、H5），可以这样增强
        // 根据请求类型调用不同的支付方法
        String payType = determinePayType(request);

        switch (payType) {
            case "JSAPI":
                return weChatPayService.jsapiPay(request);
            case "APP":
                return weChatPayService.appPay(request);
            case "NATIVE":
//                return weChatPayService.nativePay(request);
            case "H5":
                return weChatPayService.h5Pay(request);
            default:
                throw new IllegalArgumentException("不支持的支付类型: " + payType);
        }
    }

    /**
     * 根据请求参数确定支付类型
     */
    private String determinePayType(PaymentRequestDTO request) {
        // 根据业务逻辑判断支付类型
        if (request.getOpenid() != null) {
            return "JSAPI";
        } else if (request.getClientIp() != null) {
            return "H5";
        } else {
            return "APP"; // 默认APP支付
        }
    }



    @Override
    public boolean verifyCallback(PaymentCallbackDTO callback) {
        // 如果现有服务有回调验证方法，直接调用
        // 如果没有，可以在这里实现或留空
        return true; // 临时返回true，需要根据实际情况实现
    }

    @Override
    public PaymentStatusVO queryStatus(String paymentNo) {
        // 直接调用现有的查询方法
        return weChatPayService.queryPayment(paymentNo);
    }

    @Override
    public boolean closePayment(String paymentNo) {
        // 直接调用现有的关闭订单方法
        return weChatPayService.closePayment(paymentNo);
    }

    @Override
    public RefundResultVO refund(String refundNo, RefundRequestDTO request) {
        // 直接调用现有的退款方法
        return weChatPayService.refund(request);
    }
}
