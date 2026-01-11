package com.aioveu.pms.aioveu03CategoryAttribute.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: PmsCategoryAttributeQuery
 * @Description TODO  商品分类类型（规格，属性）分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/11 19:44
 * @Version 1.0
 **/

@Schema(description ="商品分类类型（规格，属性）查询对象")
@Getter
@Setter
public class PmsCategoryAttributeQuery extends BasePageQuery {

    @Schema(description = "分类ID")
    private Long categoryId;
    @Schema(description = "属性名称")
    private String name;
    @Schema(description = "类型(1:规格;2:属性;)")
    private Integer type;
}
