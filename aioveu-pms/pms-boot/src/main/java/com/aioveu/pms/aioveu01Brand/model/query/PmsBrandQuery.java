package com.aioveu.pms.aioveu01Brand.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Description: TODO 商品品牌分页查询对象
 * @Author: 雒世松
 * @Date: 2026-01-10 18:49
 * @param
 * @return:
 **/

@Schema(description = "商品品牌查询对象")
@Data
public class PmsBrandQuery extends BasePageQuery {

    @Schema(description="关键字")
    private String keywords;

    @Schema(description = "品牌名称")
    private String name;

}
