package com.aioveu.tenant.aioveu16ManagerMenuCategory.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: ManagerMenuCategory
 * @Description TODO 管理端菜单分类（多租户）实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/3 17:08
 * @Version 1.0
 **/
@Getter
@Setter
@TableName("manager_menu_category")
public class ManagerMenuCategory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 租户ID，0表示平台默认
     */
    private Long tenantId;
    /**
     * 分类标题
     */
    private String title;
    /**
     * 分类图标
     */
    private String icon;
    /**
     * 分类描述
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
     * 分类类型：workbench-工作台，sidebar-侧边栏
     */
    private String type;
    /**
     * 可见范围：0-所有用户，1-租户管理员，2-普通用户
     */
    private Integer visibleRange;
    /**
     * 是否可编辑：0-系统内置，1-可编辑
     */
    private Integer isEditable;
    /**
     * 创建人ID
     */
    private Long createBy;
    /**
     * 更新人ID
     */
    private Long updateBy;
    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;
}
