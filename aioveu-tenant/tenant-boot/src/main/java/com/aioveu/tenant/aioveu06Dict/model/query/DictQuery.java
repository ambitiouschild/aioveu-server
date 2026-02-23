package com.aioveu.tenant.aioveu06Dict.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName: DictQuery
 * @Description TODO 字典分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 19:44
 * @Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description ="字典分页查询对象")
public class DictQuery extends BasePageQuery {

    @Schema(description="关键字(字典名称)")
    private String keywords;
}
