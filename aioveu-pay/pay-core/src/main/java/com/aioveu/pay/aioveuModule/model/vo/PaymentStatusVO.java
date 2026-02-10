package com.aioveu.pay.aioveuModule.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName: PaymentStatusVO
 * @Description TODO 支付状态对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 16:48
 * @Version 1.0
 **/

@Getter
@Setter
@Builder //需要为 VO 类添加 Lombok 的构建器模式支持
@Schema( description = "支付状态对象")
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusVO implements Serializable {

    private String paymentNo;          // 支付单号
    private String thirdPaymentNo;     // 第三方支付单号
    private BigDecimal amount;         // 支付金额
    private Integer paymentStatus;      // 支付状态
    private Date paymentTime;          // 支付时间
    private String errorMessage;       // 错误信息
}
