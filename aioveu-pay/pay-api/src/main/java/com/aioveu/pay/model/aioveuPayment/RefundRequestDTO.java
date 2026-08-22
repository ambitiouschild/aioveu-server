package com.aioveu.pay.model.aioveuPayment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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


    /* =========================
     * 多租户退款路由（新增）
     * ========================= */
    @NotNull(message = "退款请求：租户ID不能为空")
    private Long tenantId;

    @NotBlank(message = "退款请求：微信应用ID不能为空")
    private String appId;

    private String paymentNo;          // 原支付单号
    private String refundNo;           // 退款单号
    private BigDecimal amount;         // 原支付金额
    private BigDecimal refundAmount;   // 退款金额
    private String refundReason;       // 退款原因
}
