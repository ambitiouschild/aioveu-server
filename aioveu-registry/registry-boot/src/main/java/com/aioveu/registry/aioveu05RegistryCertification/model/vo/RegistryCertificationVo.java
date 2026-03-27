package com.aioveu.registry.aioveu05RegistryCertification.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName: RegistryCertificationVo
 * @Description TODO 认证记录视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 18:15
 * @Version 1.0
 **/
@Getter
@Setter
@Schema( description = "认证记录视图对象")
public class RegistryCertificationVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "认证ID ")
    private Long id;
    @Schema(description = "所属租户ID")
    private Long tenantId;
    @Schema(description = "小程序账号ID")
    private Long appAccountId;
    @Schema(description = "认证类型：1-微信认证")
    private Integer certificationType;
    @Schema(description = "认证状态：0-未开始，1-申请中，2-审核中，3-审核通过，4-审核失败，5-已过期")
    private Integer certificationStatus;
    @Schema(description = "申请时间")
    private LocalDateTime applyTime;
    @Schema(description = "审核时间")
    private LocalDateTime auditTime;
    @Schema(description = "过期时间")
    private LocalDateTime expireTime;
    @Schema(description = "认证费用")
    private BigDecimal certificationFee;
    @Schema(description = "支付状态：0-未支付，1-支付中，2-支付成功，3-支付失败")
    private Integer paymentStatus;
    @Schema(description = "支付时间")
    private LocalDateTime paymentTime;
    @Schema(description = "支付订单号")
    private String paymentOrderNo;
    @Schema(description = "审核驳回原因")
    private String rejectionReason;
    @Schema(description = "审核备注")
    private String auditRemark;
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
