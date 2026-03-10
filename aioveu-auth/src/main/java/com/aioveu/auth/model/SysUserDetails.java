package com.aioveu.auth.model;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aioveu.common.constant.SecurityConstants;
import com.aioveu.common.enums.StatusEnum;
import com.aioveu.lss.api.dto.UserAuthCredentials;
import com.aioveu.system.dto.UserAuthInfo;
import com.aioveu.tenant.dto.RoleDataScope;
import com.aioveu.tenant.dto.UserAuthInfoWithTenantId;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description: TODO 系统用户信息(包含用户名、密码和权限)
 * 用户名和密码用于认证，认证成功之后授予权限
 * @Author: 雒世松
 * @Date: 2025/6/5 17:45
 * @param
 * @return:
 **/

@Slf4j
@Data
public class SysUserDetails implements UserDetails, CredentialsContainer {

    /**
     * 扩展字段：用户ID
     */
    private Long userId;

    /**
     * 默认字段
     */
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 账号是否启用(true:启用 false:禁用)
     */
    private Boolean enabled;

    /**
     * 扩展字段：部门ID
     */
    private Long deptId;

    /**
     * 用户角色数据权限集合
     */
    private Integer dataScope;

    /**
     * 数据权限列表
     * <p>
     * 存储用户所有角色的数据权限范围，用于实现多角色权限合并（并集策略）
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


    /**
     * 用户角色权限集合
     */
    private Collection<GrantedAuthority> authorities;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private Set<String> perms;


    //=================================================================================
    /**
     * 系统管理用户  使用UserAuthInfo构建
     * 构造函数：根据用户认证信息初始化用户详情对象
     */
    public SysUserDetails(UserAuthInfo user) {
        log.info("调用systemFeignClient微服务的UserAuthInfo构建Spring Security所需的UserDetails实现对象");

        this.setUserId(user.getUserId());
        this.setUsername(user.getUsername());
        this.setDeptId(user.getDeptId());
        this.setDataScope(user.getDataScope());
        this.setPassword("{bcrypt}" + user.getPassword());
        this.setEnabled(StatusEnum.ENABLE.getValue().equals(user.getStatus()));
        if (CollectionUtil.isNotEmpty(user.getRoles())) {
            authorities = user.getRoles().stream()
//                    .map(SimpleGrantedAuthority::new)   //原始不加前缀，微服务也不以前缀匹配
                    // 角色名加上前缀 "ROLE_"，用于区分角色 (ROLE_ADMIN) 和权限 (user:add)
                    .map(role -> new SimpleGrantedAuthority(SecurityConstants.ROLE_PREFIX + role))
                    .collect(Collectors.toSet());
        }
        this.setPerms(user.getPerms());
    }

    //=================================================================================
    /**
     * 构造函数：根据用户认证信息初始化用户详情对象  使用UserAuthCredentials构建
     *
     * @param user 用户认证信息对象 {@link UserAuthCredentials}
     */
    public SysUserDetails(UserAuthCredentials user) {

        log.info("调用lssFeignClient微服务的UserAuthCredentials构建Spring Security所需的UserDetails实现对象");
        this.userId = user.getUserId();
        this.username = user.getUsername();
//        this.password = user.getPassword();
        this.setPassword("{bcrypt}" + user.getPassword());
        this.enabled = ObjectUtil.equal(user.getStatus(), 1);
        this.deptId = user.getDeptId();
        this.dataScope = user.getDataScope();

        // 初始化角色权限集合
        this.authorities = CollectionUtil.isNotEmpty(user.getRoles())
                ? user.getRoles().stream()
                // 角色名加上前缀 "ROLE_"，用于区分角色 (ROLE_ADMIN) 和权限 (user:add)
                .map(role -> new SimpleGrantedAuthority(SecurityConstants.ROLE_PREFIX + role))
                .collect(Collectors.toSet())
                : Collections.emptySet();
    }

    //=================================================================================
    /**
     * 构造函数：根据用户认证信息初始化用户详情对象  使用UserAuthInfoWithTenantId构建
     *
     * @param user 用户认证信息对象 {@link UserAuthCredentials}
     */
    public SysUserDetails(UserAuthInfoWithTenantId user) {

        log.info("调用tenant微服务的UserAuthInfoWithTenantId构建Spring Security所需的UserDetails实现对象");
        this.userId = user.getUserId();
        this.username = user.getUsername();
//        this.password = user.getPassword();
        this.setPassword("{bcrypt}" + user.getPassword());
        this.enabled = ObjectUtil.equal(user.getStatus(), 1);
        this.deptId = user.getDeptId();
        this.dataScopes = user.getDataScopes();
        this.tenantId = user.getTenantId();
        this.canSwitchTenant = user.getCanSwitchTenant();

        // 初始化角色权限集合
        this.authorities = CollectionUtil.isNotEmpty(user.getRoles())
                ? user.getRoles().stream()
                // 角色名加上前缀 "ROLE_"，用于区分角色 (ROLE_ADMIN) 和权限 (user:add)
                  .map(role -> new SimpleGrantedAuthority(SecurityConstants.ROLE_PREFIX + role))
                  .collect(Collectors.toSet())
                : Collections.emptySet();
    }




    //=================================================================================

    public SysUserDetails(
            Long userId,
            String username,
            String password,
            Integer dataScope,
            Long deptId,
            boolean enabled,
            boolean accountNonExpired,
            boolean credentialsNonExpired,
            boolean accountNonLocked,
            Set<? extends GrantedAuthority> authorities
    ) {
        Assert.isTrue(username != null && !"".equals(username) && password != null,
                "Cannot pass null or empty values to constructor");
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.dataScope = dataScope;
        this.deptId = deptId;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.authorities = Collections.unmodifiableSet(authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }


    @Override
    public void eraseCredentials() {
        this.password = null;
    }
}
