package com.aioveu.tenant.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @ClassName: UserAuthInfo
 * @Description TODO 用户认证信息传输层对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/23 20:48
 * @Version 1.0
 **/
@Data
public class UserAuthInfoWithTenantId {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 密码（加密后）
     */
    private String password;

    /**
     * 状态（1:启用 其它:禁用）
     */
    private Integer status;

    /**
     * 角色集合
     */
    private Set<String> roles;

    /**
     * 状态（1:启用 其它:禁用）
     */
    private Integer dataScope;


    /**
     * 数据权限列表
     */
    private List<RoleDataScope> dataScopes;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 租户切换权限（true 可切换租户）
     */
    private Boolean canSwitchTenant;

}
