package com.aioveu.system.aioveu05Dept.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

/**
 * @ClassName: DeptForm
 * @Description TODO
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 12:43
 * @Version 1.0
 **/

@Schema(description = "部门表单对象")
@Getter
@Setter
public class DeptForm {

    @Schema(description="部门ID", example = "1001")
    private Long id;

    @Schema(description="部门名称", example = "研发部")
    private String name;

    @Schema(description="部门编号", example = "RD001")
    private String code;

    @Schema(description="父部门ID", example = "1000")
    @NotNull(message = "父部门ID不能为空")
    private Long parentId;

    @Schema(description="状态(1:启用;0:禁用)", example = "1")
    @Range(min = 0, max = 1, message = "状态值不正确")
    private Integer status;

    @Schema(description="排序(数字越小排名越靠前)", example = "1")
    private Integer sort;
}
