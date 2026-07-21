package com.aioveu.common.rabbitmq.producer.model.payment;


import com.aioveu.common.enums.pay.PaymentSceneEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName: PaymentMessage
 * @Description TODO 支付消息DTO
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/20 21:48
 * @Version 1.0
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 商户支付单号（唯一幂等键） */
    private String paymentNo;

    /** 商户支付单号（唯一幂等键） */
    private String orderSn;

    /** 支付场景 ✅ 推荐（强类型）*/
    private PaymentSceneEnum paymentSceneEnum;

    /** 第三方交易号（微信/支付宝） */
    private String thirdTransactionNo;

    /** 支付金额（单位：分） */
    private BigDecimal amount;

    /** 支付成功时间 */
    private LocalDateTime successTime;

    /** 消息产生时间 */
    private LocalDateTime eventTime;
}
