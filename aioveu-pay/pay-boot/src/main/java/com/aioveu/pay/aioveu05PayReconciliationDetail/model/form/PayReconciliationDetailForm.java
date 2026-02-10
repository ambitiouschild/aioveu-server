package com.aioveu.pay.aioveu05PayReconciliationDetail.model.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @ClassName: PayReconciliationDetailForm
 * @Description TODO 对账明细表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/9 14:23
 * @Version 1.0
 **/

@Getter
@Setter
@Schema(description = "对账明细表单对象")
public class PayReconciliationDetailForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "对账单ID")
    @NotNull(message = "对账单ID不能为空")
    private Long reconciliationId;

    @Schema(description = "渠道编码")
    @NotBlank(message = "渠道编码不能为空")
    @Size(max=20, message="渠道编码长度不能超过20个字符")
    private String channelCode;

    @Schema(description = "对账日期")
    @NotNull(message = "对账日期不能为空")
    private LocalDate billDate;

    @Schema(description = "第三方交易流水号")
    @NotBlank(message = "第三方交易流水号不能为空")
    @Size(max=128, message="第三方交易流水号长度不能超过128个字符")
    private String thirdTransactionNo;

    @Schema(description = "第三方订单号")
    @Size(max=128, message="第三方订单号长度不能超过128个字符")
    private String thirdOrderNo;

    @Schema(description = "系统支付单号")
    @Size(max=32, message="系统支付单号长度不能超过32个字符")
    private String paymentNo;

    @Schema(description = "业务订单号")
    @Size(max=32, message="业务订单号长度不能超过32个字符")
    private String orderNo;

    @Schema(description = "交易类型：PAYMENT-支付 REFUND-退款")
    @NotBlank(message = "交易类型：PAYMENT-支付 REFUND-退款不能为空")
    @Size(max=20, message="交易类型：PAYMENT-支付 REFUND-退款长度不能超过20个字符")
    private String tradeType;

    @Schema(description = "交易时间")
    @NotNull(message = "交易时间不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime tradeTime;

    @Schema(description = "交易金额")
    @NotNull(message = "交易金额不能为空")
    private BigDecimal tradeAmount;

    @Schema(description = "交易状态：SUCCESS-成功 FAIL-失败 PROCESSING-处理中")
    @NotBlank(message = "交易状态：SUCCESS-成功 FAIL-失败 PROCESSING-处理中不能为空")
    @Size(max=20, message="交易状态：SUCCESS-成功 FAIL-失败 PROCESSING-处理中长度不能超过20个字符")
    private String tradeStatus;

    @Schema(description = "对账状态：0-未对账 1-成功 2-失败 3-系统多 4-渠道多")
    @NotNull(message = "对账状态：0-未对账 1-成功 2-失败 3-系统多 4-渠道多不能为空")
    private Integer reconcileStatus;

    @Schema(description = "差异原因")
    @Size(max=200, message="差异原因长度不能超过200个字符")
    private String differenceReason;

    @Schema(description = "对账结果：MATCH-匹配 SYS_MORE-系统多 CHANNEL_MORE-渠道多")
    @Size(max=20, message="对账结果：MATCH-匹配 SYS_MORE-系统多 CHANNEL_MORE-渠道多长度不能超过20个字符")
    private String reconcileResult;

    @Schema(description = "对账时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reconcileTime;

    @Schema(description = "备注")
    @Size(max=500, message="备注长度不能超过500个字符")
    private String remark;

    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;

    @Schema(description = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
