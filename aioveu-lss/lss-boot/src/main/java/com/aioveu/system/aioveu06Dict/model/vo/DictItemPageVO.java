package com.aioveu.system.aioveu06Dict.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: DictItemPageVO
 * @Description TODO  字典项分页对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 16:59
 * @Version 1.0
 **/

@Schema(description = "字典项分页对象")
@Getter
@Setter
public class DictItemPageVO {

    @Schema(description = "字典项ID")
    private Long id;

    @Schema(description = "字典编码")
    private String dictCode;

    @Schema(description = "字典标签")
    private String label;

    @Schema(description = "字典值")
    private String value;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态（1:启用，0:禁用）")
    private Integer status;
}
