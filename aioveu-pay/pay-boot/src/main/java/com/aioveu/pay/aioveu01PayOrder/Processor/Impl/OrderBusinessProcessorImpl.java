package com.aioveu.pay.aioveu01PayOrder.Processor.Impl;


import com.aioveu.common.enums.pay.PaymentScene;
import com.aioveu.pay.aioveu01PayOrder.Processor.BusinessProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.logging.org.slf4j.MDC;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @ClassName: OrderBusinessProcessorImpl
 * @Description TODO 订单业务处理器实现（多个）
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/7/20 19:31
 * @Version 1.0
 **/

@Service
@Slf4j
public class OrderBusinessProcessorImpl implements BusinessProcessor {



    @Override
    public boolean supports(PaymentScene scene) {
        return scene == PaymentScene.ORDER;
    }


    //@Async放在「BusinessProcessor 的具体实现类」里
    @Async("bizExecutor")
    @Override
    public void onPaid(String paymentNo) {

        log.info("处理订单支付, paymentNo={}", paymentNo);
        log.info("支付成功，开始处理业务，paymentNo={}", paymentNo);

        // 1. 查订单
        // 2. 发货 / 开通权益 / 记账
        // 3. 发 MQ / 通知用户

        //一个“企业级标准模板”（直接可用）

        tring traceId = MDC.get("traceId");
        MDC.put("traceId", traceId);


        try {

            //异步方法必须幂等
            if (processed(paymentNo)) {
                return;
            }

            doBusiness(paymentNo);



            markProcessed(paymentNo);

        } catch (Exception e) {
            log.error("业务处理失败, paymentNo={}", paymentNo, e);

            //本地失败记录
            saveRetryRecord(paymentNo);
        } finally {
            MDC.clear();
        }

    }
}
