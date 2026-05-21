package com.aioveu.pay.aioveu12MqProducerPayment.model.vo;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

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


    /**
     * MQ 消息唯一ID（幂等用）
     */
//    @NotBlank(message = "消息ID不能为空")
    private String messageId;


    /**
     * 支付单号（传给微信的 out_trade_no）
     */
    @NotBlank(message = "支付单号不能为空")
    private String paymentNo;

    /**
     * 订单号（oms_order.order_sn）
     */
    @NotBlank(message = "订单号不能为空")
    private String omsOrderNo;


    /**
     * 支付金额（分 → 转 BigDecimal）
     */
    @NotNull(message = "支付金额不能为空")
    private BigDecimal paymentAmount;


    /**
     * 微信支付订单号
     */
    private String transactionId;

    /**
     * 支付成功时间（ISO-8601）
     */
    private String paymentTime;
}
