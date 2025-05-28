package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@Data
public class RoleMiniAppMenu extends IdNameEntity {

    private String roleCode;

    private String code;

    private String bgColor;

    private String icon;

    private String path;

    private Integer priority;

    private String parentCode;

    private int unread;

    @TableField(exist = false)
    private List<RoleMiniAppMenu> roleMiniAppMenuList;

}
