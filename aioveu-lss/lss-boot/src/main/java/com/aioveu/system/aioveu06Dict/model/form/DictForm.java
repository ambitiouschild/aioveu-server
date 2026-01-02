package com.aioveu.system.aioveu06Dict.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * @ClassName: DictForm
 * @Description TODO  字典表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 12:43
 * @Version 1.0
 **/

@Schema(description = "字典")
@Data
public class DictForm {

    @Schema(description = "字典ID",example = "1")
    private Long id;

    @Schema(description = "字典名称",example = "性别")
    private String name;

    @Schema(description = "字典编码", example ="gender")
    @NotBlank(message = "字典编码不能为空")
    private String dictCode;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "字典状态（1-启用，0-禁用）", example = "1")
    @Range(min = 0, max = 1, message = "字典状态不正确")
    private Integer status;
}
