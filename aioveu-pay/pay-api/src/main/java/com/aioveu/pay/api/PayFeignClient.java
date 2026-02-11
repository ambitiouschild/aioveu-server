package com.aioveu.pay.api;

import com.aioveu.common.result.Result;
import com.aioveu.common.web.config.FeignDecoderConfig;
import com.aioveu.pay.model.PaymentParamsVO;
import com.aioveu.pay.model.PaymentRequestDTO;
import com.aioveu.pay.model.PaymentStatusVO;
import com.aioveu.pay.model.RefundRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @ClassName: PayFeignClient
 * @Description TODO  支付微服务Feign 客户端
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/11 16:05
 * @Version 1.0
 **/
@FeignClient(value = "aioveu-pay", contextId = "pay", configuration = {FeignDecoderConfig.class})
public interface PayFeignClient {

    /**
     * 创建支付
     */
    @PostMapping("/api/v1/pay-order/create")
    Result<PaymentParamsVO> createPayment(@RequestBody PaymentRequestDTO request);

    /**
     * JSAPI支付
     */
    @PostMapping("/wechat/jsapi")
    Result<PaymentParamsVO> jsapiPay(@RequestBody PaymentRequestDTO request);

    /**
     * 查询支付状态
     */
    @GetMapping("/query/{orderNo}")
    Result<PaymentStatusVO> queryPayment(@PathVariable("orderNo") String orderNo);

    /**
     * 关闭支付
     */
    @PostMapping("/close/{orderNo}")
    Result<Boolean> closePayment(@PathVariable("orderNo") String orderNo);

    /**
     * 退款
     */
    @PostMapping("/refund")
    Result<Boolean> refund(@RequestBody RefundRequestDTO request);
}
