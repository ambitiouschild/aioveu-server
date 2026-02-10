package com.aioveu.pay.aioveu06PayFlow.model.form;

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
import java.time.LocalDateTime;

/**
 * @ClassName: PayFlowForm
 * @Description TODO 支付流水表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/9 15:47
 * @Version 1.0
 **/

@Getter
@Setter
@Schema(description = "支付流水表单对象")
public class PayFlowForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "流水号")
    @NotBlank(message = "流水号不能为空")
    @Size(max=32, message="流水号长度不能超过32个字符")
    private String flowNo;

    @Schema(description = "支付单号")
    @NotBlank(message = "支付单号不能为空")
    @Size(max=32, message="支付单号长度不能超过32个字符")
    private String paymentNo;

    @Schema(description = "退款单号")
    @Size(max=32, message="退款单号长度不能超过32个字符")
    private String refundNo;

    @Schema(description = "业务订单号")
    @NotBlank(message = "业务订单号不能为空")
    @Size(max=32, message="业务订单号长度不能超过32个字符")
    private String orderNo;

    @Schema(description = "用户ID")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "流水类型：PAYMENT-支付 REFUND-退款 SETTLEMENT-结算 ADJUST-调账")
    @NotBlank(message = "流水类型：PAYMENT-支付 REFUND-退款 SETTLEMENT-结算 ADJUST-调账不能为空")
    @Size(max=20, message="流水类型：PAYMENT-支付 REFUND-退款 SETTLEMENT-结算 ADJUST-调账长度不能超过20个字符")
    private String flowType;

    @Schema(description = "资金方向：IN-入金 OUT-出金")
    @NotBlank(message = "资金方向：IN-入金 OUT-出金不能为空")
    @Size(max=10, message="资金方向：IN-入金 OUT-出金长度不能超过10个字符")
    private String flowDirection;

    @Schema(description = "流水金额")
    @NotNull(message = "流水金额不能为空")
    private BigDecimal amount;

    @Schema(description = "交易前余额")
    private BigDecimal balanceBefore;

    @Schema(description = "交易后余额")
    private BigDecimal balanceAfter;

    @Schema(description = "渠道编码")
    @NotBlank(message = "渠道编码不能为空")
    @Size(max=20, message="渠道编码长度不能超过20个字符")
    private String channelCode;

    @Schema(description = "第三方流水号")
    @Size(max=128, message="第三方流水号长度不能超过128个字符")
    private String thirdFlowNo;

    @Schema(description = "流水状态：0-处理中 1-成功 2-失败")
    @NotNull(message = "流水状态：0-处理中 1-成功 2-失败不能为空")
    private Integer flowStatus;

    @Schema(description = "交易时间")
    @NotNull(message = "交易时间不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime tradeTime;

    @Schema(description = "完成时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completeTime;

    @Schema(description = "错误代码")
    @Size(max=50, message="错误代码长度不能超过50个字符")
    private String errorCode;

    @Schema(description = "错误信息")
    @Size(max=500, message="错误信息长度不能超过500个字符")
    private String errorMessage;

    @Schema(description = "备注")
    @Size(max=500, message="备注长度不能超过500个字符")
    private String remark;

    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    @NotNull(message = "逻辑删除：0-未删除 1-已删除不能为空")
    private Integer isDeleted;

    @Schema(description = "创建人")
    @Size(max=64, message="创建人长度不能超过64个字符")
    private String createBy;

    @Schema(description = "更新人")
    @Size(max=64, message="更新人长度不能超过64个字符")
    private String updateBy;

    @Schema(description = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
