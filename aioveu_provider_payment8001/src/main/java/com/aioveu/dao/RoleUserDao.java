package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.entity.RoleUser;
import com.aioveu.vo.UserRoleVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface RoleUserDao extends BaseMapper<RoleUser> {

    /**
     * 查询用户角色列表
     * @param userId
     * @return
     */
    List<UserRoleVo> getUserRole(String userId);

}
