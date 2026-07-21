package com.aioveu.pay.aioveu12MqProducerPayment.controller;


import com.aioveu.common.annotation.Log;
import com.aioveu.common.enums.LogModuleEnum;
import com.aioveu.common.result.Result;
import com.aioveu.pay.aioveu12MqProducerPayment.model.vo.SendPaymentMqDTO;
import com.aioveu.pay.aioveu12MqProducerPayment.mqProducer.MQProducerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/aioveu/api/v8/admin/pay/mq-send-message")
public class PaymentMessageController {


    private final MQProducerService MQProducerService;


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

        log.info("【Pay-mq-send-message】前端只负责“触发模拟 MQ”");
        log.info("【Pay-mq-send-message】不传金额");
        log.info("【Pay-mq-send-message】不传 transactionId");
        log.info("【Pay-mq-send-message】不传 channel");
        sendPaymentMqDTO.setManualSend(true); // ✅ 标记为人工发送
        boolean result = MQProducerService.sendPaymentSuccessMessage(sendPaymentMqDTO);
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

        boolean success = MQProducerService.sendPaymentSuccessMessage(dto);
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

        boolean success = MQProducerService.sendPaymentFailedMessage(dto);
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
        boolean result = MQProducerService.sendPaymentFailedMessage(sendPaymentMqDTO);
        return Result.judge(result);
    }

}
