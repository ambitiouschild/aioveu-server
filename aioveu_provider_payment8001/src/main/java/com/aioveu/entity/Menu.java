package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @Description 菜单表
 * @Author luyao
 * @Date: 2024-10-27 18:33:43
 */

@TableName(value = "sport_menu", autoResultMap = true)
@Data
public class Menu extends IdEntity {

    /**
     * 菜单名称
     */
    @NotEmpty(message = "名称不能为空")
    private String name;
    /**
     * 菜单code
     */
    @NotEmpty(message = "菜单编号不能为空")
    private String code;

    /**
     * 背景颜色
     */
    private String bgColor;
    /**
     * 图标
     */
    private String icon;
    /**
     * 优先级
     */
    private Integer priority;
    /**
     * 路径
     */
    private String path;
    /**
     * 父级code
     */
    private String parentCode;

    /**
     * 路由元信息
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> meta;

    /**
     * 组件名称 web端路由使用
     */
    private String component;

    /**
     * 重定向地址
     */
    private String redirect;

    /**
     * 是否隐藏
     */
    private Boolean hidden;

    /**
     * 菜单类型 0 小程序管理端菜单 1 小程序用户端菜单 2 web管理端菜单
     */
    @NotNull(message = "菜单类型不能为空")
    private Integer type;

}
