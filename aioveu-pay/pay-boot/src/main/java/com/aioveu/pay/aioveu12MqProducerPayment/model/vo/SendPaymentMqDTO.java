package com.aioveu.pay.aioveu12MqProducerPayment.model.vo;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName: SendPaymentMqDTO
 * @Description TODO 请求 DTO（专门给 MQ 用）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/20 23:44
 * @Version 1.0
 **/
@Data
public class SendPaymentMqDTO {

    @NotBlank(message = "支付订单号不能为空")
    private String payOrderNo;

    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    private String extraParams;
}
