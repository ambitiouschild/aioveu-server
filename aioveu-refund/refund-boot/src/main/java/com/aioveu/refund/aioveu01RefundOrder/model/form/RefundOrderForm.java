package com.aioveu.refund.aioveu01RefundOrder.model.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.icu.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: RefundOrderForm
 * @Description TODO  订单退款申请表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 16:22
 * @Version 1.0
 **/

@Getter
@Setter
@Schema(description = "订单退款申请表单对象")
public class RefundOrderForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "订单ID")
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @Schema(description = "订单编号")
    @NotBlank(message = "订单编号不能为空")
    @Size(max=64, message="订单编号长度不能超过64个字符")
    private String orderSn;

    @Schema(description = "退款单号")
    @NotBlank(message = "退款单号不能为空")
    @Size(max=64, message="退款单号长度不能超过64个字符")
    private String refundSn;

    @Schema(description = "用户ID")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "退款类型：1-仅退款 2-退货退款 3-换货")
    @NotNull(message = "退款类型：1-仅退款 2-退货退款 3-换货不能为空")
    private Integer refundType;

    @Schema(description = "退款原因")
    @NotBlank(message = "退款原因不能为空")
    @Size(max=200, message="退款原因长度不能超过200个字符")
    private String refundReason;

    @Schema(description = "补充说明")
    @Size(max=500, message="补充说明长度不能超过500个字符")
    private String description;

    @Schema(description = "退款凭证图片（JSON数组）")
    private String proofImages;

    @Schema(description = "申请退款金额（分）")
    @NotNull(message = "申请退款金额（分）不能为空")
    private BigDecimal refundAmount;

    @Schema(description = "实际退款金额（分）")
    private BigDecimal actualRefundAmount;

    @Schema(description = "退款状态：0-待处理 1-审核中 2-审核通过 3-审核拒绝 4-退款中 5-退款成功 6-退款失败")
    @NotNull(message = "退款状态：0-待处理 1-审核中 2-审核通过 3-审核拒绝 4-退款中 5-退款成功 6-退款失败不能为空")
    private Integer status;

    @Schema(description = "处理备注")
    @Size(max=500, message="处理备注长度不能超过500个字符")
    private String handleNote;

    @Schema(description = "处理人")
    @Size(max=50, message="处理人长度不能超过50个字符")
    private String handleBy;

    @Schema(description = "处理时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime handleTime;

    @Schema(description = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @Schema(description = "逻辑删除：0-正常 1-删除")
    private Integer deleted;

    @Schema(description = "版本号（用于乐观锁）")
    private Integer version;
}
