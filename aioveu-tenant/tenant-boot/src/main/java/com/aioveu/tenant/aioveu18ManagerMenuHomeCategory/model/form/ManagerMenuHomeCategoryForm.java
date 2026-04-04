package com.aioveu.tenant.aioveu18ManagerMenuHomeCategory.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName: ManagerMenuHomeCategoryForm
 * @Description TODO 管理端app首页分类配置表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/4 13:36
 * @Version 1.0
 **/
@Getter
@Setter
@Schema(description = "管理端app首页分类配置表单对象")
public class ManagerMenuHomeCategoryForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "管理端app分类ID")
    private Long categoryId;

    @Schema(description = "管理端app首页显示的图标URL")
    @NotBlank(message = "管理端app首页显示的图标URL不能为空")
    @Size(max=255, message="管理端app首页显示的图标URL长度不能超过255个字符")
    private String homeIcon;

    @Schema(description = "管理端app首页显示名称")
    @NotBlank(message = "管理端app首页显示名称不能为空")
    @Size(max=100, message="管理端app首页显示名称长度不能超过100个字符")
    private String homeName;

    @Schema(description = "跳转路径")
    @Size(max=255, message="跳转路径长度不能超过255个字符")
    private String jumpPath;

    @Schema(description = "跳转类型：navigateTo, redirectTo, switchTab")
    @Size(max=20, message="跳转类型：navigateTo, redirectTo, switchTab长度不能超过20个字符")
    private String jumpType;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态：0-隐藏，1-显示")
    @NotNull(message = "状态：0-隐藏，1-显示不能为空")
    private Integer status;

    @Schema(description = "备注")
    @Size(max=255, message="备注长度不能超过255个字符")
    private String remark;
}
