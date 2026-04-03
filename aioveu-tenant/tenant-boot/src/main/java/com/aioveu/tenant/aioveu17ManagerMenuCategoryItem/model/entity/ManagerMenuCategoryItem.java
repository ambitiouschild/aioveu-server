package com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: ManagerMenuCategoryItem
 * @Description TODO
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/3 17:23
 * @Version 1.0
 **/
@Getter
@Setter
@TableName("manager_menu_category_item")
public class ManagerMenuCategoryItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 租户ID，0表示平台默认
     */
    private Long tenantId;
    /**
     * 分类ID
     */
    private Long categoryId;
    /**
     * 菜单标题
     */
    private String title;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 跳转路径
     */
    private String url;
    /**
     * 路由名称
     */
    private String pathName;
    /**
     * 权限标识
     */
    private String permission;
    /**
     * 菜单描述
     */
    private String description;
    /**
     * 排序序号
     */
    private Integer sort;
    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
    /**
     * 菜单类型：0-页面，1-按钮，2-链接
     */
    private Integer type;
    /**
     * 打开方式：0-内部打开，1-新标签页
     */
    private Integer openType;
    /**
     * 是否可见
     */
    private Integer isVisible;
    /**
     * 是否系统菜单
     */
    private Integer isSystem;
    /**
     * 是否可编辑
     */
    private Integer isEditable;
    /**
     * 所需功能特性
     */

//    /**
//     * 所需功能特性
//     * 注意：MySQL JSON 列不能存空字符串，只能存 NULL 或有效 JSON
//     */
//    @TableField("required_features")
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)  // 空字符串不序列化
//    private String requiredFeatures;
    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;
    /**
     * 创建人ID
     */
    private Long createBy;
    /**
     * 更新人ID
     */
    private Long updateBy;
}
