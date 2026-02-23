package com.aioveu.tenant.aioveu03Role.model.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

/**
 * @ClassName: RolePermsBO
 * @Description TODO 角色权限集合
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 20:10
 * @Version 1.0
 **/
@Schema(description = "角色权限集合")
@Data
public class RolePermsBO {

    private Long tenantId;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 权限集合
     */
    private Set<String> perms;
}
