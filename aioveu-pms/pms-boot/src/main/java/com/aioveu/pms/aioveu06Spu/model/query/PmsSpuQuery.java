package com.aioveu.pms.aioveu06Spu.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Description: TODO 商品分页查询对象
 * @Author: 雒世松
 * @Date: 2025/6/5 18:31
 * @param
 * @return:
 **/


@Schema(description = "商品分页查询对象")
@Data
public class PmsSpuQuery extends BasePageQuery {

    @Schema(description="关键字")
    private String keywords;

    @Schema(description = "商品名称")
    private String name;

    @Schema(description="商品分类ID")
    private Long categoryId;

    @Schema(description = "商品品牌ID")
    private Long brandId;

    @Schema(description = "商品简介")
    private String description;

    @Schema(description = "商品状态(0:下架 1:上架)")
    private Integer status;

    @Schema(description="排序字段名")
    private String sortField;

    @Schema(description="排序规则(asc:升序;desc:降序)")
    private String sort;

}
