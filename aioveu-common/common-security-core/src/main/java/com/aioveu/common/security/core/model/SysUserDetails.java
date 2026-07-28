package com.aioveu.common.security.core.model;

import cn.hutool.core.collection.CollectionUtil;
import com.aioveu.common.core.constant.SecurityConstants;
import com.aioveu.common.core.enums.StatusEnum;
import com.aioveu.common.core.model.RoleDataScope;
import com.aioveu.common.security.core.model.dto.UserAuthCredentials;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
 * @Description: TODO 系统用户信息(包含用户名、密码和权限) SysUserDetails已经“企业级可用”
 * 用户名和密码用于认证，认证成功之后授予权限
 * @Author: 雒世松
 * @Date: 2025/6/5 17:45
 * @param
 * @return:
 **/

@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor   // ✅ 关键：补上无参构造
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
    private String username;  //✅ 正确命名（Spring Security 事实标准） 必须改成标准命名。
    /**
     * 密码
     */
    private String password;  //✅ 正确命名（Spring Security 事实标准）必须改成标准命名。
    /**
     * 账号是否启用(true:启用 false:禁用)
     */
    private Boolean enabled;  //enabled必须是 Boolean，不是 Integer

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
     * openId
     */
    private String openId;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 租户切换权限（true 可切换租户）
     */
    private Boolean canSwitchTenant;


    /**
     * 用户角色权限集合  这是 Spring Security 内部推荐写法
     */
    private Collection<? extends GrantedAuthority> authorities;

    private Set<String> perms;


    //=================================================================================
    /**
     * 系统管理用户  使用UserAuthInfo构建
     * 构造函数：根据用户认证信息初始化用户详情对象
     */
    public SysUserDetails(UserAuthCredentials user) {
        Assert.notNull(user, "UserAuthCredentials must not be null");
        Assert.hasText(user.getUsername(), "Username must not be empty");
        Assert.hasText(user.getPassword(), "Password must not be empty");

        this.userId = user.getUserId();
        this.username = user.getUsername();
//        this.setPassword("{bcrypt}" + user.getPassword());
        this.password = user.getPassword();  // ✅ 原样保存 让它只做“数据载体” 不要在任何地方拼 {bcrypt}

        this.enabled = StatusEnum.ENABLE.getValue().equals(user.getStatus());

        this.deptId = user.getDeptId();
        this.dataScope = user.getDataScope();
        this.dataScopes = user.getDataScopes();

        this.tenantId = user.getTenantId();
        this.canSwitchTenant = user.getCanSwitchTenant();

        this.openId = user.getOpenId();
        this.memberId = user.getMemberId();

        this.authorities = CollectionUtil.isEmpty(user.getRoles())
                ? Collections.emptySet()
                : user.getRoles().stream()
                // 角色名加上前缀 "ROLE_"，用于区分角色 (ROLE_ADMIN) 和权限 (user:add)
                  .map(role -> new SimpleGrantedAuthority(SecurityConstants.ROLE_PREFIX + role))
                  .collect(Collectors.toSet());

        this.perms = user.getPerms();
    }


    @JsonCreator
    public SysUserDetails(
            @JsonProperty("userId") Long userId,
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("enabled") Boolean enabled,
            @JsonProperty("deptId") Long deptId,
            @JsonProperty("dataScope") Integer dataScope,
            @JsonProperty("dataScopes") List<RoleDataScope> dataScopes,
            @JsonProperty("tenantId") Long tenantId,
            @JsonProperty("canSwitchTenant") Boolean canSwitchTenant,
            @JsonProperty("openId") String openId,
            @JsonProperty("memberId") Long memberId,
            @JsonProperty("authorities") Collection<? extends GrantedAuthority> authorities,
            @JsonProperty("perms") Set<String> perms  //这是“序列化声明”
    ) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.deptId = deptId;
        this.dataScope = dataScope;
        this.dataScopes = dataScopes;
        this.tenantId = tenantId;
        this.canSwitchTenant = canSwitchTenant;
        this.openId = openId;
        this.memberId = memberId;
        this.authorities = authorities != null ? authorities : Collections.emptySet();
        this.perms = perms;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities != null ? authorities : Collections.emptySet();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    //支持“账号冻结”
    @Override
    public boolean isAccountNonExpired() {

        return StatusEnum.ENABLE.getValue().equals(this.enabled);
    }

    @Override
    public boolean isAccountNonLocked() {
        return StatusEnum.ENABLE.getValue().equals(this.enabled);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return StatusEnum.ENABLE.getValue().equals(this.enabled);
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }


    @Override
    public void eraseCredentials() {
        this.password = null;
        this.perms = null;
    }
}
