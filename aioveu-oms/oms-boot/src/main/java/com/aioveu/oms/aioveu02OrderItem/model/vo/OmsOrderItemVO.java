package com.aioveu.oms.aioveu02OrderItem.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: OmsOrderItemVO
 * @Description TODO  订单商品信息视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/8 19:44
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "订单商品信息视图对象")
public class OmsOrderItemVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "订单ID")
    private Long orderId;
    @Schema(description = "商品名称")
    private String spuName;
    @Schema(description = "商品ID")
    private Long skuId;
    @Schema(description = "商品编码")
    private String skuSn;
    @Schema(description = "规格名称")
    private String skuName;
    @Schema(description = "商品图片")
    private String picUrl;
    @Schema(description = "商品单价(单位：分)")
    private Long price;
    @Schema(description = "商品数量")
    private Integer quantity;
    @Schema(description = "商品总价(单位：分)")
    private Long totalAmount;
    @Schema(description = "逻辑删除标识(1:已删除；0:正常)")
    private Integer deleted;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "修改时间")
    private LocalDateTime updateTime;
}
