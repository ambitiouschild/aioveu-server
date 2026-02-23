package com.aioveu.tenant.aioveu11Codegen.model.query;

import com.aioveu.common.base.BasePageQuery;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: TableQuery
 * @Description TODO 数据表分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 21:57
 * @Version 1.0
 **/
@Schema(description = "数据表分页查询对象")
@Data
public class TableQuery extends BasePageQuery {

    @Schema(description="关键字(表名)")
    private String keywords;

    /**
     * 排除的表名
     */
    @JsonIgnore
    private List<String> excludeTables;
}
