package com.aioveu.registry.aioveu05RegistryCertification.model.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName: RegistryCertificationForm
 * @Description TODO 认证记录表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 18:13
 * @Version 1.0
 **/
@Getter
@Setter
@Schema(description = "认证记录表单对象")
public class RegistryCertificationForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "认证ID ")
    private Long id;

    @Schema(description = "所属租户ID")
    @NotNull(message = "所属租户ID不能为空")
    private Long tenantId;

    @Schema(description = "小程序账号ID")
    @NotNull(message = "小程序账号ID不能为空")
    private Long appAccountId;

    @Schema(description = "认证类型：1-微信认证")
    private Integer certificationType;

    @Schema(description = "认证状态：0-未开始，1-申请中，2-审核中，3-审核通过，4-审核失败，5-已过期")
    @NotNull(message = "认证状态：0-未开始，1-申请中，2-审核中，3-审核通过，4-审核失败，5-已过期不能为空")
    private Integer certificationStatus;

    @Schema(description = "申请时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime applyTime;

    @Schema(description = "审核时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auditTime;

    @Schema(description = "过期时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;

    @Schema(description = "认证费用")
    @NotNull(message = "认证费用不能为空")
    private BigDecimal certificationFee;

    @Schema(description = "支付状态：0-未支付，1-支付中，2-支付成功，3-支付失败")
    @NotNull(message = "支付状态：0-未支付，1-支付中，2-支付成功，3-支付失败不能为空")
    private Integer paymentStatus;

    @Schema(description = "支付时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentTime;

    @Schema(description = "支付订单号")
    @Size(max=100, message="支付订单号长度不能超过100个字符")
    private String paymentOrderNo;

    @Schema(description = "审核驳回原因")
    @Size(max=65535, message="审核驳回原因长度不能超过65535个字符")
    private String rejectionReason;

    @Schema(description = "审核备注")
    @Size(max=65535, message="审核备注长度不能超过65535个字符")
    private String auditRemark;
}
