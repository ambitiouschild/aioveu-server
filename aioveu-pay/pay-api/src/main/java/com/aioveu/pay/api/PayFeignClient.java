package com.aioveu.pay.api;

import com.aioveu.common.result.Result;
import com.aioveu.feign.config.FeignDecoderConfig;
import com.aioveu.pay.model.aioveu01PayOrder.form.PayOrderCreateForm;
import com.aioveu.pay.model.aioveu01PayOrder.vo.PayOrderVO;
import com.aioveu.pay.model.aioveuPayment.PaymentParamsVO;
import com.aioveu.pay.model.aioveuPayment.request.PaymentRequestOmsToPayDTO;
import com.aioveu.pay.model.aioveuPayment.request.PaymentRequestPayToTPPDTO;
import com.aioveu.pay.model.aioveuPayment.PaymentStatusVO;
import com.aioveu.pay.model.aioveuPayment.RefundRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: PayFeignClient
 * @Description TODO  支付微服务Feign 客户端
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/11 16:05
 * @Version 1.0
 **/
@FeignClient(value = "aioveu-tenant-pay", contextId = "pay", configuration = {FeignDecoderConfig.class})
public interface PayFeignClient {


    /**
     * 来自oms的订单支付请求
     */
    @Operation(summary ="来自oms-pay的订单支付请求")
    @PostMapping("/aioveu/api/v8/admin/pay/pay-order/createPaymentOmsToPay")
    PaymentParamsVO createPaymentOmsToPay(@RequestBody PaymentRequestOmsToPayDTO paymentForm);


    /**
     * 来自pay-wechat的订单支付请求,创建前端调用第三方支付所需的支付参数
     */
    @Operation(summary ="来自pay-wechat的订单支付请求,创建前端调用第三方支付所需的支付参数")
    @PostMapping("/aioveu/api/v8/admin/pay/pay-order/createPaymentPayToTPP")
    PaymentParamsVO createPaymentPayToTPP(@RequestBody PaymentRequestPayToTPPDTO request);

    /**
     * JSAPI支付
     */
    @PostMapping("/wechat/jsapi")
    Result<PaymentParamsVO> jsapiPay(@RequestBody PaymentRequestPayToTPPDTO request);

    /**
     * 查询支付状态
     */
    @GetMapping("/query/{orderNo}")
    Result<PaymentStatusVO> queryPaymentStatus(@PathVariable("orderNo") String orderNo);

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


    /*
     * 新增支付订单APP
     * */
    @PostMapping("/aioveu/api/v8/app/pay/pay-order")
    String createPayOrder(@RequestBody @Valid PayOrderCreateForm formData );


    /**
     * 根据订单号查询支付订单 no = 我引用别人的
     */
    @GetMapping("/aioveu/api/v8/app/pay/pay-order/getPayOrderByOmsOrderSn")
    PayOrderVO getPayOrderByOmsOrderSn(@RequestParam("orderSn") String orderSn);

    /**
     * 根据omsOutTradeNo查询ThirdTransactionNo
     */
    @GetMapping("/aioveu/api/v8/app/pay/pay-order/getThirdTransactionNoByOmsOrderSn")
    String getThirdTransactionNoByOmsOrderSn(@RequestParam("omsOrderSn") String omsOrderSn);

}
