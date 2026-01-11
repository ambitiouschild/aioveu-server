package com.aioveu.pms.aioveu05Sku.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: PmsSkuVO
 * @Description TODO  商品库存视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/11 21:02
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "商品库存视图对象")
public class PmsSkuVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "库存ID")
    private Long id;
    @Schema(description = "商品编码")
    private String skuSn;
    @Schema(description = "SPU ID")
    private Long spuId;
    @Schema(description = "商品名称")
    private String name;
    @Schema(description = "商品规格值，以英文逗号(,)分割")
    private String specIds;
    @Schema(description = "商品价格(单位：分)")
    private Long price;
    @Schema(description = "库存数量")
    private Integer stock;
    @Schema(description = "库存锁定数量")
    private Integer lockedStock;
    @Schema(description = "商品图片地址")
    private String picUrl;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
