package com.aioveu.pay.aioveu12MqProducerPayment.controller;


import com.aioveu.common.annotation.Log;
import com.aioveu.common.enums.LogModuleEnum;
import com.aioveu.common.result.Result;
import com.aioveu.pay.aioveu01.model.vo.PaymentParamsVO;
import com.aioveu.pay.aioveu01.model.vo.PaymentRequestDTO;
import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu12MqProducerPayment.model.vo.SendPaymentMqDTO;
import com.aioveu.pay.aioveu12MqProducerPayment.service.PaymentMessageService;
import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @ClassName: PaymentMQProducer
 * @Description TODO 创建MQ生产者服务
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/11 19:33
 * @Version 1.0
 **/

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/pay-send-message")
public class PaymentMessageController {


    private final PaymentMessageService paymentMessageService;


    /*
    *  发送支付成功消息并保存发送记录
    * */
    @Operation(summary ="发送支付成功消息并保存发送记录")
    @PostMapping("/success")
//    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrder:oms-order:query')")
    @Log( value = "发送支付成功消息并保存发送记录",module = LogModuleEnum.PAY)

    public Result<Void> sendPaymentSuccessMessage(
            @Parameter(description = "请求 DTO") @Valid @RequestBody SendPaymentMqDTO sendPaymentMqDTO
    ) {
        boolean result = paymentMessageService.sendPaymentSuccessMessage(sendPaymentMqDTO);
        return Result.judge(result);
    }




    /*
    *
    *           ✅ 只测 MQ + 订单
                ✅ 不涉及微信
                ✅ 最安全
    * */
    @PostMapping("/mock-callback-success")
    public String mockPaySuccess(@RequestParam String paymentNo) {

        SendPaymentMqDTO dto = new SendPaymentMqDTO();
        dto.setPaymentNo(paymentNo);

        boolean success = paymentMessageService.sendPaymentSuccessMessage(dto);
        return success ? "ok" : "fail";
    }


    /*
    *
    *           ✅ 只测 MQ + 订单
                ✅ 不涉及微信
                ✅ 最安全
    * */
    @PostMapping("/mock-callback-failed")
    public String mockPayFailed(@RequestParam String paymentNo) {

        SendPaymentMqDTO dto = new SendPaymentMqDTO();
        dto.setPaymentNo(paymentNo);

        boolean success = paymentMessageService.sendPaymentFailedMessage(dto);
        return success ? "ok" : "fail";
    }


    /*
     *  发送支付失败消息并保存发送记录
     * */
    @Operation(summary ="发送支付失败消息并保存发送记录")
    @PostMapping("/fail")
//    @PreAuthorize("@ss.hasPerm('aioveuMallOmsOrder:oms-order:query')")
    @Log( value = "发送支付失败消息并保存发送记录",module = LogModuleEnum.PAY)

    public Result<Void> sendPaymentFailedMessage(
            @Parameter(description = "请求 DTO") @Valid @RequestBody SendPaymentMqDTO sendPaymentMqDTO
    ) {
        boolean result = paymentMessageService.sendPaymentFailedMessage(sendPaymentMqDTO);
        return Result.judge(result);
    }

}
