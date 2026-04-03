package com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: ManagerMenuCategoryItemVo
 * @Description TODO 管理系统工作台菜单项（多租户支持）视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/3 17:26
 * @Version 1.0
 **/
@Getter
@Setter
@Schema( description = "管理系统工作台菜单项（多租户支持）视图对象")
public class ManagerMenuCategoryItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "租户ID，0表示平台默认")
    private Long tenantId;
    @Schema(description = "分类ID")
    private Long categoryId;
    @Schema(description = "菜单标题")
    private String title;
    @Schema(description = "菜单图标")
    private String icon;
    @Schema(description = "跳转路径")
    private String url;
    @Schema(description = "路由名称")
    private String pathName;
    @Schema(description = "权限标识")
    private String permission;
    @Schema(description = "菜单描述")
    private String description;
    @Schema(description = "排序序号")
    private Integer sort;
    @Schema(description = "状态：0-禁用，1-启用")
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
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    @Schema(description = "创建人ID")
    private Long createBy;
    @Schema(description = "更新人ID")
    private Long updateBy;
}
