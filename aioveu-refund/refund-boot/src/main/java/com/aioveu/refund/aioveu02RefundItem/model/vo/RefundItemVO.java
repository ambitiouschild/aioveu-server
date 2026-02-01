package com.aioveu.refund.aioveu02RefundItem.model.vo;

import com.ibm.icu.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: RefundItemVO
 * @Description TODO 退款商品明细视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 16:43
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "退款商品明细视图对象")
public class RefundItemVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "退款申请ID")
    private Long refundId;
    @Schema(description = "退款类型（冗余字段，与主表一致）")
    private Integer refundType;
    @Schema(description = "订单项ID")
    private Long orderItemId;
    @Schema(description = "商品ID")
    private Long spuId;
    @Schema(description = "商品名称")
    private String spuName;
    @Schema(description = "SKU ID")
    private Long skuId;
    @Schema(description = "SKU名称")
    private String skuName;
    @Schema(description = "商品图片")
    private String picUrl;
    @Schema(description = "商品单价（分）")
    private BigDecimal price;
    @Schema(description = "退款数量")
    private Integer quantity;
    @Schema(description = "退款金额（分）")
    private BigDecimal refundAmount;
    @Schema(description = "该商品的退款原因")
    private String refundReason;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
    @Schema(description = "逻辑删除")
    private Integer deleted;
}
