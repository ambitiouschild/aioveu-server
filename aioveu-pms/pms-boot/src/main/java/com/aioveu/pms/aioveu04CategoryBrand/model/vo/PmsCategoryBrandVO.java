package com.aioveu.pms.aioveu04CategoryBrand.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName: PmsCategoryBrandVO
 * @Description TODO 商品分类与品牌关联表视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/11 20:05
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "商品分类与品牌关联表视图对象")
public class PmsCategoryBrandVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "商品分类")
    private Long categoryId;
    @Schema(description = "商品品牌")
    private Long brandId;
}
