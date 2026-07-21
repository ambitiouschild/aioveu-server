package com.aioveu.pay.aioveu00Payment.Processor.Impl;


import com.aioveu.common.enums.pay.PaymentSceneEnum;
import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu00Payment.Processor.BusinessProcessor;
import com.aioveu.pay.aioveu01PayOrder.service.PayOrderService;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName: BusinessProcessorComposite
 * @Description TODO 核心：Composite 组合器（灵魂）
 *
                         * ✅ Spring 自动注入所有 BusinessProcessor
                         *
                         * ✅ 顺序可控（加 @Order）
                         *
                         * ✅ 失败隔离
 *
 *
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/7/20 19:31
 * @Version 1.0
 **/

@Component
@Slf4j
@AllArgsConstructor
public class BusinessProcessorComposite implements BusinessProcessor {


    @Resource
    private List<BusinessProcessor> processors;


    private final PayOrderService payOrderService;

    @Override
    public boolean supports(PaymentSceneEnum scene) {
        return true;
    }

    @Override
    public void onPaid(String paymentNo) {

        PayOrder order = payOrderService.selectByPaymentNo(paymentNo);
        if (order == null) {
            return;
        }

        PaymentSceneEnum scene = order.getPaymentScene();

        if (scene == null) {
            log.error("未知支付场景, paymentNo={}", paymentNo);
            return;
        }

        for (BusinessProcessor processor : processors) {
            try {
                if (processor.supports(scene)) {
                    processor.onPaid(paymentNo);
                }
            } catch (Exception e) {
                // ✅ 单个失败不影响其他
                log.error("业务处理器执行失败, class={}, paymentNo={}",
                        processor.getClass().getSimpleName(),
                        paymentNo, e);
            }
        }
    }
}
