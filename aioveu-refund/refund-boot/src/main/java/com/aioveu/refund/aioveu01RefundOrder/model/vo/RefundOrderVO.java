package com.aioveu.refund.aioveu01RefundOrder.model.vo;

import com.ibm.icu.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: RefundOrderVO
 * @Description TODO 订单退款申请视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 16:23
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "订单退款申请视图对象")
public class RefundOrderVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "订单ID")
    private Long orderId;
    @Schema(description = "订单编号")
    private String orderSn;
    @Schema(description = "退款单号")
    private String refundSn;
    @Schema(description = "用户ID")
    private Long userId;
    @Schema(description = "退款类型：1-仅退款 2-退货退款 3-换货")
    private Integer refundType;
    @Schema(description = "退款原因")
    private String refundReason;
    @Schema(description = "补充说明")
    private String description;
    @Schema(description = "退款凭证图片（JSON数组）")
    private String proofImages;
    @Schema(description = "申请退款金额（分）")
    private BigDecimal refundAmount;
    @Schema(description = "实际退款金额（分）")
    private BigDecimal actualRefundAmount;
    @Schema(description = "退款状态：0-待处理 1-审核中 2-审核通过 3-审核拒绝 4-退款中 5-退款成功 6-退款失败")
    private Integer status;
    @Schema(description = "处理备注")
    private String handleNote;
    @Schema(description = "处理人")
    private String handleBy;
    @Schema(description = "处理时间")
    private LocalDateTime handleTime;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
    @Schema(description = "逻辑删除：0-正常 1-删除")
    private Integer deleted;
    @Schema(description = "版本号（用于乐观锁）")
    private Integer version;
}
