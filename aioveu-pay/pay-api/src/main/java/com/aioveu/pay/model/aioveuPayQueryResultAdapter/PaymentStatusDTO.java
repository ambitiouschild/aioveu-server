package com.aioveu.pay.model.aioveuPayQueryResultAdapter;

import com.aioveu.common.enums.pay.PaymentStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName: PaymentStatusDTO
 * @Description TODO 支付状态对象  ✅ 内部统一模型
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 16:48
 * @Version 1.0
 **/

@Getter
@Setter
@Builder //需要为 VO 类添加 Lombok 的构建器模式支持
@Schema( description = "支付状态对象✅ 内部统一模型")
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class PaymentStatusDTO implements Serializable {

    /** 支付单号 */
    private String paymentNo;          // 支付单号

    /** 第三方交易号 */
    private String thirdPaymentNo;     // 第三方支付单号

    /** 支付金额（元） */
    private BigDecimal amount;         // 支付金额

    /** 支付状态（数字） */
    private PaymentStatusEnum paymentStatus;      // 支付状态

    /** 支付状态文案（可选） */
    private String paymentStatusText;

    /** 支付金额（元） */
    private LocalDateTime paymentTime;          // 支付时间

    /** 错误信息 */
    private String errorMessage;       // 错误信息
}
