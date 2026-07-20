package com.aioveu.pay.aioveu01PayOrder.Processor.Impl;

import com.aioveu.common.enums.pay.PaymentScene;
import com.aioveu.pay.aioveu01PayOrder.Processor.BusinessProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


/**
 * @ClassName: MembershipBusinessProcessorImpl
 * @Description TODO 会员业务处理器实现（多个）
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/7/20 19:31
 * @Version 1.0
 **/

@Service
@Slf4j
public class MembershipBusinessProcessorImpl implements BusinessProcessor {



    @Override
    public boolean supports(PaymentScene scene) {
        return scene == PaymentScene.MEMBERSHIP;
    }


    //@Async放在「BusinessProcessor 的具体实现类」里
    @Async("bizExecutor")
    @Override
    public void onPaid(String paymentNo) {
        // 开通会员权益
    }


}
