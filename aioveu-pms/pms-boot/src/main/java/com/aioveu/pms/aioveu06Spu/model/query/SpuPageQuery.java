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
public class SpuPageQuery extends BasePageQuery {

    @Schema(description="关键字")
    private String keywords;

    @Schema(description="商品分类ID")
    private Long categoryId;

    @Schema(description="排序字段名")
    private String sortField;

    @Schema(description="排序规则(asc:升序;desc:降序)")
    private String sort;

}
