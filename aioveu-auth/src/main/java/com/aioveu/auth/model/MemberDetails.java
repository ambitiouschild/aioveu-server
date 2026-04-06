package com.aioveu.auth.model;

import com.aioveu.common.constant.GlobalConstants;
import com.aioveu.ums.dto.MemberAuthDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * @Description: TODO 会员信息
 * @Author: 雒世松
 * @Date: 2025/6/5 17:44
 * @param
 * @return:
 **/

@NoArgsConstructor  // ✅ Lombok 生成无参构造函数
@AllArgsConstructor // ✅ Lombok 生成全参构造函数
@Data
public class MemberDetails implements UserDetails {

    /**
     * 会员ID
     */
    private Long id;

    /**
     * 会员用户名(openid/mobile)
     */
    private String username;

    /**
     * 租户ID  // 新增：租户ID
     */
    private Long tenantId;

    /**
     * // 新增：微信小程序ID
     */
    private String wxAppid;

    /**
     * 会员状态
     */
    private Boolean enabled;

    /**
     * 扩展字段：认证身份标识，枚举值如下：
     *
     */
    private String authenticationIdentity;


    // ✅ 添加无参构造函数
//    public MemberDetails() {
//    }

    /**
     * 会员信息构造
     *
     * @param memAuthInfo 会员认证信息
     */
    public MemberDetails(MemberAuthDTO memAuthInfo) {
        this.setId(memAuthInfo.getId());
        this.setUsername(memAuthInfo.getUsername());
        this.setTenantId(memAuthInfo.getTenantId());
        this.setEnabled(GlobalConstants.STATUS_YES.equals(memAuthInfo.getStatus()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.EMPTY_SET;
    }

    @Override
    public String getPassword() {
        return null;
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
}
