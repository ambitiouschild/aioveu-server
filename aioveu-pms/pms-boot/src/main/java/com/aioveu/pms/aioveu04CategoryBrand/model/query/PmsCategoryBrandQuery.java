package com.aioveu.pms.aioveu04CategoryBrand.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: PmsCategoryBrandQuery
 * @Description TODO  商品分类与品牌关联表分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/11 20:04
 * @Version 1.0
 **/

@Schema(description ="商品分类与品牌关联表查询对象")
@Getter
@Setter
public class PmsCategoryBrandQuery extends BasePageQuery {

    @Schema(description = "商品分类")
    private Long categoryId;
    @Schema(description = "商品品牌")
    private Long brandId;
}
