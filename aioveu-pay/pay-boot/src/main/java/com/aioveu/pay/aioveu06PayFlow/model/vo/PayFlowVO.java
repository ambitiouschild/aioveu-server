package com.aioveu.pay.aioveu06PayFlow.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName: PayFlowVO
 * @Description TODO 支付流水视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/9 15:49
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "支付流水视图对象")
public class PayFlowVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "流水号")
    private String flowNo;
    @Schema(description = "支付单号")
    private String paymentNo;
    @Schema(description = "退款单号")
    private String refundNo;
    @Schema(description = "业务订单号")
    private String orderNo;
    @Schema(description = "用户ID")
    private Long userId;
    @Schema(description = "流水类型：PAYMENT-支付 REFUND-退款 SETTLEMENT-结算 ADJUST-调账")
    private String flowType;
    @Schema(description = "资金方向：IN-入金 OUT-出金")
    private String flowDirection;
    @Schema(description = "流水金额")
    private BigDecimal amount;
    @Schema(description = "交易前余额")
    private BigDecimal balanceBefore;
    @Schema(description = "交易后余额")
    private BigDecimal balanceAfter;
    @Schema(description = "渠道编码")
    private String channelCode;
    @Schema(description = "第三方流水号")
    private String thirdFlowNo;
    @Schema(description = "流水状态：0-处理中 1-成功 2-失败")
    private Integer flowStatus;
    @Schema(description = "交易时间")
    private LocalDateTime tradeTime;
    @Schema(description = "完成时间")
    private LocalDateTime completeTime;
    @Schema(description = "错误代码")
    private String errorCode;
    @Schema(description = "错误信息")
    private String errorMessage;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
    @Schema(description = "创建人")
    private String createBy;
    @Schema(description = "更新人")
    private String updateBy;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
