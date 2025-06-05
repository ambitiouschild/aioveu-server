package com.aioveu.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.system.model.entity.SysUserRole;

import java.util.List;

/**
 * @Description: TODO 用户角色关联业务接口
 * @Author: 雒世松
 * @Date: 2025/6/5 17:30
 * @param
 * @return:
 **/

public interface SysUserRoleService extends IService<SysUserRole> {

    /**
     * 保存用户角色
     *
     * @param userId 用户ID
     * @param roleIds 角色ID集合
     * @return boolean 是否保存成功
     */
     boolean saveUserRoles(Long userId, List<Long> roleIds);


    /**
     * 判断角色是否存在绑定的用户
     *
     * @param roleId 角色ID
     * @return true：已分配 false：未分配
     */
    boolean hasAssignedUsers(Long roleId);
}
