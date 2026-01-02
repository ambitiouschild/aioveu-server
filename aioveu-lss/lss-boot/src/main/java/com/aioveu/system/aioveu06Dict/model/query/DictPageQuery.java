package com.aioveu.system.aioveu06Dict.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName: DictPageQuery
 * @Description TODO
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 16:55
 * @Version 1.0
 **/

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description ="字典分页查询对象")
public class DictPageQuery extends BasePageQuery {

    @Schema(description="关键字(字典名称)")
    private String keywords;
}
