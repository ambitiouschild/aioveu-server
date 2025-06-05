package com.aioveu.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @Description: TODO 角色和菜单关联表
 * @Author: 雒世松
 * @Date: 2025/6/5 17:21
 * @param null
 * @return:
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysRoleMenu  {
    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}