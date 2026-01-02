package com.aioveu.system.aioveu06Dict.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName: DictItemPageQuery
 * @Description TODO
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 16:54
 * @Version 1.0
 **/

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description ="字典项分页查询对象")
public class DictItemPageQuery extends BasePageQuery {

    @Schema(description="关键字(字典项值/字典项名称)")
    private String keywords;

    @Schema(description="字典编码")
    private String dictCode;
}
