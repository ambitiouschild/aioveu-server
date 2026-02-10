package com.aioveu.pay.aioveuModule.channelRouter;

import com.aioveu.pay.aioveuModule.PaymentStrategy.PaymentStrategy;
import com.aioveu.pay.aioveuModule.PaymentStrategy.impl.PaymentStrategyFactory;
import com.aioveu.pay.aioveuModule.model.vo.PaymentRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName: ChannelRouter
 * @Description TODO 渠道路由
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 20:58
 * @Version 1.0
 **/

@Component
public class ChannelRouter {

    @Autowired
    private PaymentStrategyFactory strategyFactory;

    /**
     * 根据渠道选择支付方式
     */
    public PaymentStrategy route(String channel) {
        PaymentStrategy strategy = strategyFactory.getStrategy(channel);
        if (strategy == null) {
            throw new BusinessException("不支持的支付渠道");
        }
        return strategy;
    }

    /**
     * 智能选择支付渠道
     */
    public String smartRoute(PaymentRequestDTO request) {
        // 根据用户历史支付习惯
        // 根据渠道可用性
        // 根据手续费率
        // 根据支付成功率
        return determineBestChannel(request);
    }
}
