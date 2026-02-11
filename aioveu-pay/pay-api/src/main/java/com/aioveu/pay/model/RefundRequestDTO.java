package com.aioveu.pay.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

/**
 * @ClassName: RefundRequestDTO
 * @Description TODO 退款申请对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 17:18
 * @Version 1.0
 **/

@Getter
@Setter
@Builder //需要为 VO 类添加 Lombok 的构建器模式支持
@Schema( description = "退款申请对象")
@NoArgsConstructor
@AllArgsConstructor
public class RefundRequestDTO {

    private String paymentNo;          // 原支付单号
    private String refundNo;           // 退款单号
    private BigDecimal amount;         // 原支付金额
    private BigDecimal refundAmount;   // 退款金额
    private String refundReason;       // 退款原因
}
