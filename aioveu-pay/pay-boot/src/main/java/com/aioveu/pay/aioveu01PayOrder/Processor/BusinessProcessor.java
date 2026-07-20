package com.aioveu.pay.aioveu01PayOrder.Processor;


import com.aioveu.common.enums.pay.PaymentScene;

/**
 * @ClassName: OrderBusinessProcessor
 * @Description TODO OrderBusinessProcessor= 支付成功后的业务动作统一入口
                         * 比如：
                         *
                         * 发货
                         *
                         * 开通会员
                         *
                         * 记账
                         *
                         * 发消息
                         *
                         * 增加积分
                         * 好处：
                         *
                         * ✅ 支付模块 零业务依赖
                         *
                         * ✅ 新业务只需新实现 BusinessProcessor
                         *
                         * ✅ 回调 / Job / 轮询 共用同一套逻辑
                         *
                         * ✅ 方便做幂等、事务、MQ 异步
                         *
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/7/20 19:31
 * @Version 1.0
 **/


public interface BusinessProcessor {



    //进阶：多个业务处理器（企业级）
    /**
     * 是否支持该支付场景
     */
    boolean supports(PaymentScene scene);


    /*
    * List<BusinessProcessor> processors;
     processors.stream()
    .filter(p -> p.supports(scene))
    .forEach(p -> p.onPaid(paymentNo));
        ✅ 支付中心 / SaaS / 多业务线必备
    *
    *
    *
    * */
    /**
     * 支付成功回调
     */
    void onPaid(String paymentNo);
}
