package com.aioveu.system.mapper;

import com.aioveu.system.model.entity.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: TODO 用户角色访问层
 * @Author: 雒世松
 * @Date: 2025/6/5 17:17
 * @param
 * @return:
 **/

@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    /**
     * 获取角色绑定的用户数
     *
     * @param roleId 角色ID
     */
    int countUsersForRole(Long roleId);
}
