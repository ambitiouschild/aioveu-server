package com.aioveu.system.aioveu03Role.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: RoleMenu
 * @Description TODO  角色和菜单关联表
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 12:40
 * @Version 1.0
 **/

@TableName("sys_role_menu")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenu {

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;
}
