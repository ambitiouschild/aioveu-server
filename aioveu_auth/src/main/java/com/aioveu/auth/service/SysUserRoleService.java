package com.aioveu.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.auth.model.po.SysUserRole;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2020/11/17 10:42
 */
public interface SysUserRoleService extends IService<SysUserRole> {


    /**
     * 查找用户角色
     * @param userId
     * @return
     */
    List<SysUserRole> findByUserId(String userId);

}
