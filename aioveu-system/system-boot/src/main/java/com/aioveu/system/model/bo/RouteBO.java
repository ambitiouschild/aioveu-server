package com.aioveu.system.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.aioveu.system.enums.MenuTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * @Description: TODO 路由
 * @Author: 雒世松
 * @Date: 2025/6/5 17:17
 * @param
 * @return:
 **/

@Data
public class RouteBO {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单类型(1-菜单；2-目录；3-外链；4-按钮权限)
     */
    private MenuTypeEnum type;

    /**
     * 路由路径(浏览器地址栏路径)
     */
    private String path;

    /**
     * 组件路径(vue页面完整路径，省略.vue后缀)
     */
    private String component;

    /**
     * 权限标识
     */
    private String perm;

    /**
     * 显示状态(1:显示;0:隐藏)
     */
    private Integer visible;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 跳转路径
     */
    private String redirect;

    /**
     * 拥有路由的权限
     */
    private List<String> roles;

    /**
     * 【目录】只有一个子路由是否始终显示(1:是 0:否)
     */
    private Integer alwaysShow;

    /**
     * 【菜单】是否开启页面缓存(1:是 0:否)
     */
    private Integer keepAlive;

}