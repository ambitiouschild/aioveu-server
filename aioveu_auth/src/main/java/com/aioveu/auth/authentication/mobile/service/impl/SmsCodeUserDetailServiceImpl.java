package com.aioveu.auth.authentication.mobile.service.impl;

import com.aioveu.auth.authentication.mobile.service.SmsCodeUserDetailService;
import com.aioveu.auth.model.po.SysUser;
import com.aioveu.auth.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * userDetailService实现类（手机号+密码授权类型）
 */
@Service
public class SmsCodeUserDetailServiceImpl implements SmsCodeUserDetailService {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public UserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException {
        SysUser user = sysUserService.findByMobileAndStatus(mobile, 1);
        if (Objects.isNull(user))
            throw new UsernameNotFoundException("账号不存在！");
        return sysUserService.getByUser(user);
    }
}
