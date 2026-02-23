package com.aioveu.tenant.aioveu08Config.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName: ConfigQuery
 * @Description TODO 系统配置查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 19:41
 * @Version 1.0
 **/

@Data
@Schema(description = "系统配置分页查询")
public class ConfigQuery extends BasePageQuery {

    @Schema(description="关键字(配置项名称/配置项值)")
    private String keywords;
}
