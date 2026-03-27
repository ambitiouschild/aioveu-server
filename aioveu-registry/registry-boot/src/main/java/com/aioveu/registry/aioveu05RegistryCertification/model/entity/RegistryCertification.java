package com.aioveu.registry.aioveu05RegistryCertification.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName: RegistryCertification
 * @Description TODO 认证记录实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 18:13
 * @Version 1.0
 **/
@Getter
@Setter
@TableName("registry_certification")
public class RegistryCertification extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 所属租户ID
     */
    private Long tenantId;
    /**
     * 小程序账号ID
     */
    private Long appAccountId;
    /**
     * 认证类型：1-微信认证
     */
    private Integer certificationType;
    /**
     * 认证状态：0-未开始，1-申请中，2-审核中，3-审核通过，4-审核失败，5-已过期
     */
    private Integer certificationStatus;
    /**
     * 申请时间
     */
    private LocalDateTime applyTime;
    /**
     * 审核时间
     */
    private LocalDateTime auditTime;
    /**
     * 过期时间
     */
    private LocalDateTime expireTime;
    /**
     * 认证费用
     */
    private BigDecimal certificationFee;
    /**
     * 支付状态：0-未支付，1-支付中，2-支付成功，3-支付失败
     */
    private Integer paymentStatus;
    /**
     * 支付时间
     */
    private LocalDateTime paymentTime;
    /**
     * 支付订单号
     */
    private String paymentOrderNo;
    /**
     * 审核驳回原因
     */
    private String rejectionReason;
    /**
     * 审核备注
     */
    private String auditRemark;
    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;
}
