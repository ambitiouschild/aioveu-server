package com.aioveu.pay.aioveu05PayReconciliationDetail.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @ClassName: PayReconciliationDetail
 * @Description TODO  对账明细实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/9 14:14
 * @Version 1.0
 **/

@Getter
@Setter
@TableName("pay_reconciliation_detail")
public class PayReconciliationDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 对账单ID
     */
    private Long reconciliationId;
    /**
     * 渠道编码
     */
    private String channelCode;
    /**
     * 对账日期
     */
    private LocalDate billDate;
    /**
     * 第三方交易流水号
     */
    private String thirdTransactionNo;
    /**
     * 第三方订单号
     */
    private String thirdOrderNo;
    /**
     * 系统支付单号
     */
    private String paymentNo;
    /**
     * 业务订单号
     */
    private String orderNo;
    /**
     * 交易类型：PAYMENT-支付 REFUND-退款
     */
    private String tradeType;
    /**
     * 交易时间
     */
    private LocalDateTime tradeTime;
    /**
     * 交易金额
     */
    private BigDecimal tradeAmount;
    /**
     * 交易状态：SUCCESS-成功 FAIL-失败 PROCESSING-处理中
     */
    private String tradeStatus;
    /**
     * 对账状态：0-未对账 1-成功 2-失败 3-系统多 4-渠道多
     */
    private Integer reconcileStatus;
    /**
     * 差异原因
     */
    private String differenceReason;
    /**
     * 对账结果：MATCH-匹配 SYS_MORE-系统多 CHANNEL_MORE-渠道多
     */
    private String reconcileResult;
    /**
     * 对账时间
     */
    private LocalDateTime reconcileTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;
}
