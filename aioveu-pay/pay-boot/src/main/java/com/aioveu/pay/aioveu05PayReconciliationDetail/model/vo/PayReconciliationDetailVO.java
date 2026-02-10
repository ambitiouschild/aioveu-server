package com.aioveu.pay.aioveu05PayReconciliationDetail.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @ClassName: PayReconciliationDetailVO
 * @Description TODO 对账明细视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/9 14:24
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "对账明细视图对象")
public class PayReconciliationDetailVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "对账单ID")
    private Long reconciliationId;
    @Schema(description = "渠道编码")
    private String channelCode;
    @Schema(description = "对账日期")
    private LocalDate billDate;
    @Schema(description = "第三方交易流水号")
    private String thirdTransactionNo;
    @Schema(description = "第三方订单号")
    private String thirdOrderNo;
    @Schema(description = "系统支付单号")
    private String paymentNo;
    @Schema(description = "业务订单号")
    private String orderNo;
    @Schema(description = "交易类型：PAYMENT-支付 REFUND-退款")
    private String tradeType;
    @Schema(description = "交易时间")
    private LocalDateTime tradeTime;
    @Schema(description = "交易金额")
    private BigDecimal tradeAmount;
    @Schema(description = "交易状态：SUCCESS-成功 FAIL-失败 PROCESSING-处理中")
    private String tradeStatus;
    @Schema(description = "对账状态：0-未对账 1-成功 2-失败 3-系统多 4-渠道多")
    private Integer reconcileStatus;
    @Schema(description = "差异原因")
    private String differenceReason;
    @Schema(description = "对账结果：MATCH-匹配 SYS_MORE-系统多 CHANNEL_MORE-渠道多")
    private String reconcileResult;
    @Schema(description = "对账时间")
    private LocalDateTime reconcileTime;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
