package com.aioveu.system.aioveu03Role.model.vo;

import lombok.Data;

import java.util.Set;

/**
 * @ClassName: RolePermsBO
 * @Description TODO  角色权限业务对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 18:07
 * @Version 1.0
 **/

@Data
public class RolePermsBO {

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 权限标识集合
     */
    private Set<String> perms;
}
