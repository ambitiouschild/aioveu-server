package com.aioveu.pms.aioveu07SpuAttribute.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: PmsSpuAttributeQuery
 * @Description TODO 商品类型（属性/规格）分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/11 22:04
 * @Version 1.0
 **/

@Schema(description ="商品类型（属性/规格）查询对象")
@Getter
@Setter
public class PmsSpuAttributeQuery extends BasePageQuery {

    @Schema(description = "产品ID")
    private Long spuId;
    @Schema(description = "属性ID")
    private Long attributeId;
    @Schema(description = "属性名称")
    private String name;
    @Schema(description = "类型(1:规格;2:属性;)")
    private Integer type;
}
