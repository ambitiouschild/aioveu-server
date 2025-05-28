package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 角色菜单权限实体类
 */
@Data
@TableName("sport_role_menu_permission")
public class RoleMenuPermission extends IdEntity {

    @NotEmpty(message = "角色code不能为空")
    private String roleCode;

    @NotEmpty(message = "菜单code不能为空")
    private String menuCode;

    @NotNull(message = "权限id不能为空")
    private Long permissionId;

}
