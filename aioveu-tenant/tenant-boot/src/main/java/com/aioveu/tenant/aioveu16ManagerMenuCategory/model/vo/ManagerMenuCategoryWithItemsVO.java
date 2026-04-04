package com.aioveu.tenant.aioveu16ManagerMenuCategory.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: ManagerMenuCategoryWithItemsVO
 * @Description TODO  小程序管理端分类及菜单项VO
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/3 21:29
 * @Version 1.0
 **/
@Data
@Schema(description = "小程序管理端分类及菜单项VO")
public class ManagerMenuCategoryWithItemsVO implements Serializable{

    @Schema(description = "分类ID")
    private Long id;

    @Schema(description = "分类标题")
    private String title;

    @Schema(description = "分类图标")
    private String icon;

    @Schema(description = "排序序号")
    private Integer sort;

    @Schema(description = "状态：0-禁用，1-启用")
    private Integer status;

    @Schema(description = "菜单项列表")
    private List<MenuItemVO> children;

    /**
     * 菜单项VO
     */
    @Data
    @Schema(description = "菜单项VO")
    public static class MenuItemVO implements Serializable {

        @Schema(description = "菜单项ID")
        private Long id;

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
    }
}
