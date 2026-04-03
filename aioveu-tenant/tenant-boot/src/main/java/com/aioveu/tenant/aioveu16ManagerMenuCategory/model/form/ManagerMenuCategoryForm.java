package com.aioveu.tenant.aioveu16ManagerMenuCategory.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName: ManagerMenuCategoryForm
 * @Description TODO 管理端菜单分类（多租户）表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/3 17:09
 * @Version 1.0
 **/
@Getter
@Setter
@Schema(description = "管理端菜单分类（多租户）表单对象")
public class ManagerMenuCategoryForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "租户ID，0表示平台默认")
    @NotNull(message = "租户ID，0表示平台默认不能为空")
    private Long tenantId;

    @Schema(description = "分类标题")
    @NotBlank(message = "分类标题不能为空")
    @Size(max=100, message="分类标题长度不能超过100个字符")
    private String title;

    @Schema(description = "分类图标")
    @Size(max=255, message="分类图标长度不能超过255个字符")
    private String icon;

    @Schema(description = "分类描述")
    @Size(max=500, message="分类描述长度不能超过500个字符")
    private String description;

    @Schema(description = "排序序号")
    @NotNull(message = "排序序号不能为空")
    private Integer sort;

    @Schema(description = "状态：0-禁用，1-启用")
    private Integer status;

    @Schema(description = "分类类型：workbench-工作台，sidebar-侧边栏")
    @Size(max=50, message="分类类型：workbench-工作台，sidebar-侧边栏长度不能超过50个字符")
    private String type;

    @Schema(description = "可见范围：0-所有用户，1-租户管理员，2-普通用户")
    private Integer visibleRange;

    @Schema(description = "是否可编辑：0-系统内置，1-可编辑")
    private Integer isEditable;

    @Schema(description = "创建人ID")
    private Long createBy;

    @Schema(description = "更新人ID")
    private Long updateBy;

}
