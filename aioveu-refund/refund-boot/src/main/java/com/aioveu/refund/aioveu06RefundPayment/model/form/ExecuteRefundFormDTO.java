package com.aioveu.refund.aioveu06RefundPayment.model.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.icu.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: ExecuteRefundFormDTO
 * @Description TODO 退款支付记录表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 14:20
 * @Version 1.0
 **/

@Getter
@Setter
@Schema(description = "退款支付记录表单对象")
public class ExecuteRefundFormDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "退款申请ID")
    @NotNull(message = "退款申请ID不能为空")
    private Long refundId;

    @Schema(description = "退款支付单号")
    @NotBlank(message = "退款支付单号不能为空")
    @Size(max=64, message="退款支付单号长度不能超过64个字符")
    private String paymentSn;

    @Schema(description = "支付类型：1-微信 2-支付宝 3-银行卡 4-余额")
    @NotNull(message = "支付类型：1-微信 2-支付宝 3-银行卡 4-余额不能为空")
    private Integer paymentType;

    @Schema(description = "支付金额（分）")
    @NotNull(message = "支付金额（分）不能为空")
    private BigDecimal paymentAmount;

    @Schema(description = "支付状态：0-待支付 1-支付中 2-支付成功 3-支付失败")
    @NotNull(message = "支付状态：0-待支付 1-支付中 2-支付成功 3-支付失败不能为空")
    private Integer paymentStatus;

    @Schema(description = "支付时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentTime;

    @Schema(description = "支付渠道")
    @Size(max=50, message="支付渠道长度不能超过50个字符")
    private String paymentChannel;

    @Schema(description = "支付交易号")
    @Size(max=100, message="支付交易号长度不能超过100个字符")
    private String paymentTradeNo;

    @Schema(description = "支付手续费（分）")
    private BigDecimal paymentFee;

    @Schema(description = "备注")
    @Size(max=200, message="备注长度不能超过200个字符")
    private String remark;

    @Schema(description = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
