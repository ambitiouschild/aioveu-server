package com.aioveu.pay.aioveuModule.service.AliPay.strategy;

import com.aioveu.pay.aioveuModule.PaymentStrategy.PaymentStrategy;
import com.aioveu.pay.aioveuModule.model.vo.PaymentParamsVO;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: AlipayStrategy
 * @Description TODO 支付宝支付策略
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 20:56
 * @Version 1.0
 **/

// 支付宝支付策略
@Service
@Slf4j
public class AlipayStrategy implements PaymentStrategy {

    @Autowired
    private AlipayClient alipayClient;

    @Override
    public PaymentParamsVO pay(String paymentNo, PaymentRequestDTO request) {
        try {
            // 构建支付宝请求
            AlipayTradeAppPayRequest aliRequest = new AlipayTradeAppPayRequest();
            aliRequest.setNotifyUrl(config.getNotifyUrl());
            aliRequest.setReturnUrl(request.getReturnUrl());

            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
            model.setOutTradeNo(paymentNo);
            model.setTotalAmount(request.getAmount().toString());
            model.setSubject(request.getSubject());
            model.setBody(request.getBody());
            model.setTimeExpire(getExpireTime(request.getExpireMinutes()));

            aliRequest.setBizModel(model);

            // 调用支付宝SDK
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(aliRequest);

            if (response.isSuccess()) {
                return PaymentParamsVO.builder()
                        .paymentNo(paymentNo)
                        .paymentParams(response.getBody())
                        .build();
            } else {
                throw new BusinessException("支付宝支付失败: " + response.getMsg());
            }

        } catch (Exception e) {
            log.error("支付宝支付异常", e);
            throw new BusinessException("支付宝支付异常");
        }
    }
}
