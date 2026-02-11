package com.aioveu.pay.aioveu06PayFlow.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName: PayFlow
 * @Description TODO 支付流水实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/9 15:46
 * @Version 1.0
 **/

@Getter
@Setter
@Builder
@TableName("pay_flow")
public class PayFlow extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 流水号
     */
    private String flowNo;
    /**
     * 支付单号
     */
    private String paymentNo;
    /**
     * 退款单号
     */
    private String refundNo;
    /**
     * 业务订单号
     */
    private String orderNo;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 流水类型：PAYMENT-支付 REFUND-退款 SETTLEMENT-结算 ADJUST-调账
     */
    private String flowType;
    /**
     * 资金方向：IN-入金 OUT-出金
     */
    private String flowDirection;
    /**
     * 流水金额
     */
    private BigDecimal amount;
    /**
     * 交易前余额
     */
    private BigDecimal balanceBefore;
    /**
     * 交易后余额
     */
    private BigDecimal balanceAfter;
    /**
     * 渠道编码
     */
    private String channelCode;
    /**
     * 第三方流水号
     */
    private String thirdFlowNo;
    /**
     * 流水状态：0-处理中 1-成功 2-失败
     */
    private Integer flowStatus;
    /**
     * 交易时间
     */
    private LocalDateTime tradeTime;
    /**
     * 完成时间
     */
    private LocalDateTime completeTime;
    /**
     * 错误代码
     */
    private String errorCode;
    /**
     * 错误信息
     */
    private String errorMessage;
    /**
     * 备注
     */
    private String remark;
    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 更新人
     */
    private String updateBy;
}
