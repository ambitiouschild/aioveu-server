package com.aioveu.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.aioveu.auth.model.po.SysUser;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @description
 * @author: 雒世松
 * @date: 2020/11/17 10:42
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 用户名获取用户
     * @param username
     * @param status
     * @return
     */
    SysUser findByUsernameAndStatus(String username, Integer status);

    /**
     * 手机号码获取用户
     * @param phone
     * @param status
     * @return
     */
    SysUser findByMobileAndStatus(String phone, Integer status);

    /**
     * 电话查找用户
     * @param phone
     * @return
     */
    UserDetails getByPhone(String phone);

    /**
     * 手机号码快速注册
     * @param phone
     * @return
     */
    UserDetails quickRegisterByPhone(String phone);

    /**
     * 根据openId查找用户
     * @param openId
     * @return
     */
    UserDetails getByOpenId(String openId);

    /**
     * 获取认证用户对象
     * @param sysUser
     * @return
     */
    UserDetails getByUser(SysUser sysUser);

}
