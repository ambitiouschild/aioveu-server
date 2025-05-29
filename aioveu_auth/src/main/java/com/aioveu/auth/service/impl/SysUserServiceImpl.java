package com.aioveu.auth.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.auth.common.model.SecurityUser;
import com.aioveu.auth.common.model.SysConstant;
import com.aioveu.auth.dao.SysUserDao;
import com.aioveu.auth.model.po.SysUser;
import com.aioveu.auth.model.po.SysUserRole;
import com.aioveu.auth.service.SysUserRoleService;
import com.aioveu.auth.service.SysUserService;
import com.aioveu.auth.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2020/11/17 10:42
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUser> implements SysUserService {

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public SysUser findByUsernameAndStatus(String username, Integer status) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUser::getUsername, username)
                .eq(SysUser::getStatus, status);
        return getOne(queryWrapper);
    }

    @Override
    public SysUser findByMobileAndStatus(String phone, Integer status) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUser::getPhone, phone)
                .eq(SysUser::getStatus, status);
        return getOne(queryWrapper);
    }

    @Override
    public UserDetails getByPhone(String phone) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUser::getPhone, phone);
        SysUser sysUser = getOne(queryWrapper);
        return getByUser(sysUser);
    }

    @Override
    public UserDetails quickRegisterByPhone(String phone) {
        SysUser sysUser = new SysUser();
        sysUser.setHead("default/head_boy.png");
        sysUser.setPhone(phone);
        sysUser.setUsername(phone);
        sysUser.setPassword(passwordEncoder.encode(UUID.fastUUID().toString(true)));
        sysUser.setStatus(1);
        // 设置昵称 隐藏中间手机号
        sysUser.setName(phone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
        if (save(sysUser)) {
            return getByUser(sysUser);
        }
        return null;
    }

    @Override
    public UserDetails getByUser(SysUser sysUser) {
        if (sysUser!= null) {
            //角色
            List<SysUserRole> sysUserRoles = sysUserRoleService.findByUserId(sysUser.getId());
            //该用户的所有权限（角色）
            List<String> roles = new ArrayList<>();
            for (SysUserRole userRole : sysUserRoles) {
                roles.add(SysConstant.ROLE_PREFIX + userRole.getRoleCode());
            }
            return SecurityUser.builder()
                    .id(sysUser.getId())
                    .username(sysUser.getUsername())
                    .password(sysUser.getPassword())
                    .status(sysUser.getStatus())
                    .createTime(sysUser.getCreateDate())
                    .credentialsExpired(sysUser.getCredentialsExpired())
                    .accountExpiredTime(sysUser.getAccountExpiredTime())
                    .name(sysUser.getName())
                    .phone(sysUser.getPhone())
                    .email(sysUser.getMail())
                    .gender(sysUser.getGender())
                    .head(FileUtil.getImageFullUrl(sysUser.getHead()))
                    //将角色放入authorities中
                    .authorities(AuthorityUtils.createAuthorityList(ArrayUtil.toArray(roles, String.class)))
                    .build();
        }
        return null;
    }


    @Override
    public UserDetails getByOpenId(String openId) {
        SysUser sysUser = getBaseMapper().getByOpenId(openId);
        return getByUser(sysUser);
    }
}
