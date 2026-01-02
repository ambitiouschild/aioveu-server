package com.aioveu.system.aioveu02User.mapper;

import com.aioveu.system.aioveu02User.model.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
  *@ClassName: UserRoleMapper
  *@Description TODO  用户角色访问层
  *@Author 可我不敌可爱
  *@Author 雒世松
  *@Date 2025/12/31 18:01
  *@Version 1.0
  **/
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 获取角色绑定的用户数
     *
     * @param roleId 角色ID
     */
    int countUsersForRole(Long roleId);
}
