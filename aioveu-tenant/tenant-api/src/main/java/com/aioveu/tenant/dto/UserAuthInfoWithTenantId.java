package com.aioveu.tenant.dto;

import com.aioveu.common.core.model.RoleDataScope;
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
     * 租户ID
     */
    private Long tenantId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 密码（加密后）
     */
    private String password;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 状态（1:启用 其它:禁用）
     */
    private Integer status;

    /**
     * 角色集合
     */
    private Set<String> roles;

    /**
     * 状态（1:启用 其它:禁用）   // ✅ 数据权限（范围）
     */
    private Integer dataScope;


    /**
     * 数据权限列表  // ✅ 数据权限明细
     */
    private List<RoleDataScope> dataScopes;


    /**
     * 接口权限标识集合（按钮权限）
     */
    private Set<String> permissions;

    /**
     * 租户切换权限（true 可切换租户）
     */
    private Boolean canSwitchTenant;

}
