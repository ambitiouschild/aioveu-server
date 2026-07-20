package com.aioveu.pay.aioveu01PayOrder.Processor.Impl;


import com.aioveu.common.enums.pay.PaymentScene;
import com.aioveu.pay.aioveu01PayOrder.Processor.BusinessProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @ClassName: AccountBusinessProcessorImpl
 * @Description TODO 账务业务处理器实现（多个）（所有支付都要）
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/7/20 19:31
 * @Version 1.0
 **/

@Service
@Slf4j
public class AccountBusinessProcessorImpl implements BusinessProcessor {


    @Override
    public boolean supports(PaymentScene scene) {
        // ✅ 所有场景都支持
        return true;
    }


    //@Async放在「BusinessProcessor 的具体实现类」里
    @Async("bizExecutor")
    @Override
    public void onPaid(String paymentNo) {
        // 记财务流水
    }
}
