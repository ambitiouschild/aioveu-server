package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Description 角色菜单关联表
 * @Author luyao
 * @Date: 2024-10-27 18:30:47
 */

@TableName("sport_role_menu")
@Data
public class RoleMenu extends IdEntity {

    /**
     * 角色code
     */
    private String roleCode;

    /**
     * 菜单code
     */
    private String menuCode;


}
