package com.aioveu.system.aioveu06Dict.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName: DictItemForm
 * @Description TODO  字典项表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 12:44
 * @Version 1.0
 **/

@Schema(description = "字典项表单")
@Data
public class DictItemForm {

    @Schema(description = "字典项ID")
    private Long id;

    @Schema(description = "字典编码")
    private String dictCode;

    @Schema(description = "字典项值")
    private String value;

    @Schema(description = "字典项标签")
    private String label;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态（0：禁用，1：启用）")
    private Integer status;

    @Schema(description = "字典类型（用于显示样式）")
    private String tagType;
}
