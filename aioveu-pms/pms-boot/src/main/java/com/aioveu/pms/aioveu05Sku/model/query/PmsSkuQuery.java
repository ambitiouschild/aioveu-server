package com.aioveu.pms.aioveu05Sku.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: PmsSkuQuery
 * @Description TODO  商品库存分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/11 21:01
 * @Version 1.0
 **/

@Schema(description ="商品库存查询对象")
@Getter
@Setter
public class PmsSkuQuery extends BasePageQuery {

    @Schema(description = "商品编码")
    private String skuSn;
    @Schema(description = "SPU ID")
    private Long spuId;
    @Schema(description = "商品名称")
    private String name;
    @Schema(description = "商品规格值，以英文逗号(,)分割")
    private String specIds;
}
