package com.aioveu.auth.model;

import com.aioveu.common.constant.GlobalConstants;
import com.aioveu.ums.dto.MemberAuthDTO;
import lombok.Data;
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
     * 会员状态
     */
    private Boolean enabled;

    /**
     * 扩展字段：认证身份标识，枚举值如下：
     *
     */
    private String authenticationIdentity;

    /**
     * 会员信息构造
     *
     * @param memAuthInfo 会员认证信息
     */
    public MemberDetails(MemberAuthDTO memAuthInfo) {
        this.setId(memAuthInfo.getId());
        this.setUsername(memAuthInfo.getUsername());
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
