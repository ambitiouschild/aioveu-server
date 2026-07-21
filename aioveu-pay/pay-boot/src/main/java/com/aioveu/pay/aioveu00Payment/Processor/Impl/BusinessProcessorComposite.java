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
public class BusinessProcessorComposite implements BusinessProcessor {


    //是 Spring 在启动时，把所有 BusinessProcessor实现类塞进了 List<BusinessProcessor>
    //Composite只是按顺序遍历，问每个 Processor：“你支持这个场景吗？”
    //✅ 谁说 supports(scene) == true，谁就干活
    @Resource
    private List<BusinessProcessor> processors;


    private final PayOrderService payOrderService;

    public BusinessProcessorComposite(PayOrderService payOrderService,List<BusinessProcessor> processors) {
        this.payOrderService = payOrderService;
        // ✅ 把自己排除掉
        this.processors = processors.stream()
                .filter(p -> !(p instanceof BusinessProcessorComposite))
                .toList();
    }

    @Override
    public boolean supports(PaymentSceneEnum scene) {
        return true;// 我全都要
    }




    @Override
    public void onPaid(String paymentNo) {

        PayOrder order = payOrderService.selectByPaymentNo(paymentNo);
        if (order == null) {
            return;
        }

        PaymentSceneEnum scene = order.getPaymentScene();

        if (scene == null) {
            log.error("【BusinessProcessorComposite】未知支付场景, paymentNo={}", paymentNo);
            return;
        }


        //processors= Spring 注入的 List<BusinessProcessor>
        log.error("【BusinessProcessorComposite】关键点：processors= Spring 注入的 List<BusinessProcessor>");
        for (BusinessProcessor processor : processors) {
            try {
                if (processor.supports(scene)) {
                    processor.onPaid(paymentNo);
                }
            } catch (Exception e) {
                // ✅ 单个失败不影响其他
                log.error("【BusinessProcessorComposite】业务处理器执行失败, class={}, paymentNo={}",
                        processor.getClass().getSimpleName(),
                        paymentNo, e);
            }
        }
    }
}
