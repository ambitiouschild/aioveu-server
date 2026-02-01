package com.aioveu.refund.aioveu06RefundPayment.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ibm.icu.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @ClassName: RefundPayment
 * @Description TODO 退款支付记录实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 13:26
 * @Version 1.0
 **/

@Getter
@Setter
@TableName("refund_payment")
public class RefundPayment extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 退款申请ID
     */
    private Long refundId;
    /**
     * 退款支付单号
     */
    private String paymentSn;
    /**
     * 支付类型：1-微信 2-支付宝 3-银行卡 4-余额
     */
    private Integer paymentType;
    /**
     * 支付金额（分）
     */
    private BigDecimal paymentAmount;
    /**
     * 支付状态：0-待支付 1-支付中 2-支付成功 3-支付失败
     */
    private Integer paymentStatus;
    /**
     * 支付时间
     */
    private LocalDateTime paymentTime;
    /**
     * 支付渠道
     */
    private String paymentChannel;
    /**
     * 支付交易号
     */
    private String paymentTradeNo;
    /**
     * 支付手续费（分）
     */
    private BigDecimal paymentFee;
    /**
     * 备注
     */
    private String remark;
}
