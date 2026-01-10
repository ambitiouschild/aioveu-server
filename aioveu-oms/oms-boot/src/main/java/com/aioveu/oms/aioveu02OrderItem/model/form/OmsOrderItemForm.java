package com.aioveu.oms.aioveu02OrderItem.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName: OmsOrderItemForm
 * @Description TODO  订单商品信息表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/8 19:41
 * @Version 1.0
 **/

@Getter
@Setter
@Schema(description = "订单商品信息表单对象")
public class OmsOrderItemForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "商品名称")
    @Size(max=128, message="商品名称长度不能超过128个字符")
    private String spuName;

    @Schema(description = "商品ID")
    @NotNull(message = "商品ID不能为空")
    private Long skuId;

    @Schema(description = "商品编码")
    @NotBlank(message = "商品编码不能为空")
    @Size(max=64, message="商品编码长度不能超过64个字符")
    private String skuSn;

    @Schema(description = "规格名称")
    @NotBlank(message = "规格名称不能为空")
    @Size(max=128, message="规格名称长度不能超过128个字符")
    private String skuName;

    @Schema(description = "商品图片")
    @NotBlank(message = "商品图片不能为空")
    @Size(max=255, message="商品图片长度不能超过255个字符")
    private String picUrl;

    @Schema(description = "商品单价(单位：分)")
    @NotNull(message = "商品单价(单位：分)不能为空")
    private Long price;

    @Schema(description = "商品数量")
    private Integer quantity;

    @Schema(description = "商品总价(单位：分)")
    @NotNull(message = "商品总价(单位：分)不能为空")
    private Long totalAmount;

    @Schema(description = "逻辑删除标识(1:已删除；0:正常)")
    private Integer deleted;
}
