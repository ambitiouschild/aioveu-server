package com.aioveu.tenant.aioveu06Dict.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName: DictItemQuery
 * @Description TODO 字典项分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 19:43
 * @Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description ="字典项分页查询对象")
public class DictItemQuery extends BasePageQuery {

    @Schema(description="关键字(字典项值/字典项名称)")
    private String keywords;

    @Schema(description="字典编码")
    private String dictCode;
}
