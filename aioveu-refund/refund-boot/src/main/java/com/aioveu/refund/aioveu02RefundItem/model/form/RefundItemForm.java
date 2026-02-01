package com.aioveu.refund.aioveu02RefundItem.model.form;

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
 * @ClassName: RefundItemForm
 * @Description TODO  退款商品明细表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 16:41
 * @Version 1.0
 **/


@Getter
@Setter
@Schema(description = "退款商品明细表单对象")
public class RefundItemForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "退款申请ID")
    @NotNull(message = "退款申请ID不能为空")
    private Long refundId;

    @Schema(description = "退款类型（冗余字段，与主表一致）")
    private Integer refundType;

    @Schema(description = "订单项ID")
    @NotNull(message = "订单项ID不能为空")
    private Long orderItemId;

    @Schema(description = "商品ID")
    @NotNull(message = "商品ID不能为空")
    private Long spuId;

    @Schema(description = "商品名称")
    @NotBlank(message = "商品名称不能为空")
    @Size(max=200, message="商品名称长度不能超过200个字符")
    private String spuName;

    @Schema(description = "SKU ID")
    @NotNull(message = "SKU ID不能为空")
    private Long skuId;

    @Schema(description = "SKU名称")
    @Size(max=200, message="SKU名称长度不能超过200个字符")
    private String skuName;

    @Schema(description = "商品图片")
    @Size(max=500, message="商品图片长度不能超过500个字符")
    private String picUrl;

    @Schema(description = "商品单价（分）")
    @NotNull(message = "商品单价（分）不能为空")
    private BigDecimal price;

    @Schema(description = "退款数量")
    @NotNull(message = "退款数量不能为空")
    private Integer quantity;

    @Schema(description = "退款金额（分）")
    @NotNull(message = "退款金额（分）不能为空")
    private BigDecimal refundAmount;

    @Schema(description = "该商品的退款原因")
    @Size(max=200, message="该商品的退款原因长度不能超过200个字符")
    private String refundReason;

    @Schema(description = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @Schema(description = "逻辑删除")
    private Integer deleted;
}
