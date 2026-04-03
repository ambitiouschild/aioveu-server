package com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName: ManagerMenuCategoryItemForm
 * @Description TODO 管理系统工作台菜单项（多租户支持）表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/3 17:25
 * @Version 1.0
 **/
@Getter
@Setter
@Schema(description = "管理系统工作台菜单项（多租户支持）表单对象")
public class ManagerMenuCategoryItemForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "租户ID，0表示平台默认")
    @NotNull(message = "租户ID，0表示平台默认不能为空")
    private Long tenantId;

    @Schema(description = "分类ID")
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    @Schema(description = "菜单标题")
    @NotBlank(message = "菜单标题不能为空")
    @Size(max=100, message="菜单标题长度不能超过100个字符")
    private String title;

    @Schema(description = "菜单图标")
    @Size(max=255, message="菜单图标长度不能超过255个字符")
    private String icon;

    @Schema(description = "跳转路径")
    @NotBlank(message = "跳转路径不能为空")
    @Size(max=500, message="跳转路径长度不能超过500个字符")
    private String url;

    @Schema(description = "路由名称")
    @Size(max=100, message="路由名称长度不能超过100个字符")
    private String pathName;

    @Schema(description = "权限标识")
    @Size(max=200, message="权限标识长度不能超过200个字符")
    private String permission;

    @Schema(description = "菜单描述")
    @Size(max=500, message="菜单描述长度不能超过500个字符")
    private String description;

    @Schema(description = "排序序号")
    @NotNull(message = "排序序号不能为空")
    private Integer sort;

    @Schema(description = "状态：0-禁用，1-启用")
    @NotNull(message = "状态：0-禁用，1-启用不能为空")
    private Integer status;

    @Schema(description = "菜单类型：0-页面，1-按钮，2-链接")
    private Integer type;

    @Schema(description = "打开方式：0-内部打开，1-新标签页")
    private Integer openType;

    @Schema(description = "是否可见")
    private Integer isVisible;

    @Schema(description = "是否系统菜单")
    private Integer isSystem;

    @Schema(description = "是否可编辑")
    private Integer isEditable;

//    @Schema(description = "所需功能特性")
//    private String requiredFeatures;

    @Schema(description = "创建人ID")
    private Long createBy;

    @Schema(description = "更新人ID")
    private Long updateBy;
}
