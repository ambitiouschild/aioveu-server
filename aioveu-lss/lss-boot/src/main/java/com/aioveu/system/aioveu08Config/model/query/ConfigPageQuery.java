package com.aioveu.system.aioveu08Config.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: ConfigPageQuery
 * @Description TODO  系统配置查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 12:47
 * @Version 1.0
 **/

@Getter
@Setter
@Schema(description = "系统配置分页查询")
public class ConfigPageQuery extends BasePageQuery {

    @Schema(description="关键字(配置项名称/配置项值)")
    private String keywords;
}
