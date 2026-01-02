package com.aioveu.system.aioveu08Config.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName: ConfigVO
 * @Description TODO  系统配置视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 16:57
 * @Version 1.0
 **/

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Schema(description = "系统配置VO")
public class ConfigVO {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "配置名称")
    private String configName;

    @Schema(description = "配置键")
    private String configKey;

    @Schema(description = "配置值")
    private String configValue;

    @Schema(description = "描述、备注")
    private String remark;
}
