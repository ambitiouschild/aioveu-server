package com.aioveu.pms.aioveu05Sku.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName: PmsSkuForm
 * @Description TODO 商品库存表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/11 21:01
 * @Version 1.0
 **/

@Getter
@Setter
@Schema(description = "商品库存表单对象")
public class PmsSkuForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "库存ID")
    private Long id;

    @Schema(description = "商品编码")
    @NotBlank(message = "商品编码不能为空")
    @Size(max=64, message="商品编码长度不能超过64个字符")
    private String skuSn;

    @Schema(description = "SPU ID")
    @NotNull(message = "SPU ID不能为空")
    private Long spuId;

    @Schema(description = "商品名称")
    @NotBlank(message = "商品名称不能为空")
    @Size(max=128, message="商品名称长度不能超过128个字符")
    private String name;

    @Schema(description = "商品规格值，以英文逗号(,)分割")
    @Size(max=255, message="商品规格值，以英文逗号(,)分割长度不能超过255个字符")
    private String specIds;

    @Schema(description = "商品价格(单位：分)")
    private Long price;

    @Schema(description = "库存数量")
    private Integer stock;

    @Schema(description = "库存锁定数量")
    private Integer lockedStock;

    @Schema(description = "商品图片地址")
    @Size(max=255, message="商品图片地址长度不能超过255个字符")
    private String picUrl;
}
