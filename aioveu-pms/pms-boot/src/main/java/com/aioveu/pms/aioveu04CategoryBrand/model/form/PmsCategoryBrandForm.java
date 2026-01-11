package com.aioveu.pms.aioveu04CategoryBrand.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName: PmsCategoryBrandForm
 * @Description TODO  商品分类与品牌关联表表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/11 20:04
 * @Version 1.0
 **/

@Getter
@Setter
@Schema(description = "商品分类与品牌关联表表单对象")
public class PmsCategoryBrandForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "商品分类")
    @NotNull(message = "商品分类不能为空")
    private Long categoryId;

    @Schema(description = "商品品牌")
    @NotNull(message = "商品品牌不能为空")
    private Long brandId;

}
