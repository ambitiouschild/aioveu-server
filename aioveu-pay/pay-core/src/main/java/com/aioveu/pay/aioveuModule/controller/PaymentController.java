package com.aioveu.pay.aioveuModule.controller;

import com.aioveu.pay.aioveuModule.PaymentStrategy.PaymentStrategy;
import com.aioveu.pay.aioveuModule.PaymentStrategy.impl.PaymentStrategyFactory;
import com.aioveu.pay.aioveuModule.model.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: PaymentController
 * @Description TODO
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 23:13
 * @Version 1.0
 **/

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentStrategyFactory strategyFactory;

    @PostMapping("/pay/{channel}")
    public PaymentParamsVO pay(@PathVariable String channel,
                               @RequestBody PaymentRequestDTO request) {
        String paymentNo = generatePaymentNo();

        PaymentStrategy strategy = strategyFactory.getStrategy(channel);
        return strategy.appPay(paymentNo, request);
    }

    @GetMapping("/status/{channel}/{paymentNo}")
    public PaymentStatusVO queryStatus(@PathVariable String channel,
                                       @PathVariable String paymentNo) {
        PaymentStrategy strategy = strategyFactory.getStrategy(channel);
        return strategy.queryStatus(paymentNo);
    }

    @PostMapping("/refund/{channel}")
    public RefundResultVO refund(@PathVariable String channel,
                                 @RequestBody RefundRequestDTO request) {
        String refundNo = generateRefundNo();
        PaymentStrategy strategy = strategyFactory.getStrategy(channel);
        return strategy.refund(refundNo, request);
    }

    private String generatePaymentNo() {
        return "P" + System.currentTimeMillis();
    }

    private String generateRefundNo() {
        return "R" + System.currentTimeMillis();
    }
}
