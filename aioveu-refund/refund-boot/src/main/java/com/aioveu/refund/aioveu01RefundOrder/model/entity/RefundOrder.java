package com.aioveu.refund.aioveu01RefundOrder.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ibm.icu.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @ClassName: RefundOrder
 * @Description TODO  订单退款申请实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 16:20
 * @Version 1.0
 **/

@Getter
@Setter
@TableName("refund_order")
public class RefundOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    private Long orderId;
    /**
     * 订单编号
     */
    private String orderSn;
    /**
     * 退款单号
     */
    private String refundSn;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 退款类型：1-仅退款 2-退货退款 3-换货
     */
    private Integer refundType;
    /**
     * 退款原因
     */
    private String refundReason;
    /**
     * 补充说明
     */
    private String description;
    /**
     * 退款凭证图片（JSON数组）
     */
    private String proofImages;
    /**
     * 申请退款金额（分）
     */
    private BigDecimal refundAmount;
    /**
     * 实际退款金额（分）
     */
    private BigDecimal actualRefundAmount;
    /**
     * 退款状态：0-待处理 1-审核中 2-审核通过 3-审核拒绝 4-退款中 5-退款成功 6-退款失败
     */
    private Integer status;
    /**
     * 处理备注
     */
    private String handleNote;
    /**
     * 处理人
     */
    private String handleBy;
    /**
     * 处理时间
     */
    private LocalDateTime handleTime;
    /**
     * 逻辑删除：0-正常 1-删除
     */
    private Integer deleted;
    /**
     * 版本号（用于乐观锁）
     */
    private Integer version;
}
