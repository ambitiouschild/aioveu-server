package com.aioveu.tenant.aioveu02User.service;

import com.aioveu.tenant.aioveu02User.model.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @ClassName: UserRoleService
 * @Description TODO
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 23:26
 * @Version 1.0
 **/
public interface UserRoleService extends IService<UserRole> {

    /**
     * 保存用户角色
     *
     * @param userId
     * @param roleIds
     * @return
     */
    void saveUserRoles(Long userId, List<Long> roleIds);

    /**
     * 判断角色是否存在绑定的用户
     *
     * @param roleId 角色ID
     * @return true：已分配 false：未分配
     */
    boolean hasAssignedUsers(Long roleId);

    /**
     * 获取角色绑定的用户ID集合
     *
     * @param roleId 角色ID
     * @return 用户ID集合
     */
    List<Long> listUserIdsByRoleId(Long roleId);
}
