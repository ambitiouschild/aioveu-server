package com.aioveu.system.model.bo;

import lombok.Data;

import java.util.Set;

/**
 * @Description: TODO 角色权限业务对象
 * @Author: 雒世松
 * @Date: 2025/6/5 17:17
 * @param
 * @return:
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
