package com.aioveu.auth.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.aioveu.auth.common.model.SecurityUser;
import com.aioveu.auth.common.model.SysConstant;
import com.aioveu.auth.model.po.SysUser;
import com.aioveu.auth.model.po.SysUserRole;
import com.aioveu.auth.service.SysUserRoleService;
import com.aioveu.auth.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author 雒世松
 * UserDetailsService的实现类，从数据库加载用户的信息，比如密码、角色。。。。
 */
@Service(value = "jwtTokenUserDetailsServiceV2")
@Primary
public class JwtTokenUserDetailsServiceV2 implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = sysUserService.findByUsernameAndStatus(username, 1);
        if (Objects.isNull(user))
            throw new UsernameNotFoundException("用户不存在！");
        //角色
        List<SysUserRole> sysUserRoles = sysUserRoleService.findByUserId(user.getId());
        //该用户的所有权限（角色）
        List<String> roles=new ArrayList<>();
        for (SysUserRole userRole : sysUserRoles) {
            roles.add(SysConstant.ROLE_PREFIX + userRole.getRoleCode());
        }
        return SecurityUser.builder()
                .id(user.getId())
                .status(user.getStatus())
                .username(user.getUsername())
                .password(user.getPassword())
                //将角色放入authorities中
                .authorities(AuthorityUtils.createAuthorityList(ArrayUtil.toArray(roles, String.class)))
                .build();
    }
}
