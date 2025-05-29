package com.aioveu.auth.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

/**
 * @description 用户认证类
 * @author: 雒世松
 * @date: 2020/2/3 0003 14:37
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecurityUser implements UserDetails {

    private String id;

    private String name;

    private String username;

    private String password;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    private String permissions;

    private Integer status;

    private Date accountExpiredTime;

    private Date credentialsExpired;

    private String head;

    private String phone;

    private String email;

    /**
     * 0 未知 1 男性 2 女性
     */
    private Integer gender;

    //权限+角色集合
    private Collection<? extends GrantedAuthority> authorities;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        if (accountExpiredTime == null){
            return true;
        }
        return accountExpiredTime.after(new Date());
    }

    @Override
    public boolean isAccountNonLocked() {
        return getStatus() != 2;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        if (credentialsExpired==null){
            return true;
        }
        return credentialsExpired.after(new Date());
    }

    @Override
    public boolean isEnabled() {
        return getStatus() != 0;
    }

}
