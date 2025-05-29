package com.aioveu.auth.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.auth.model.entity.SysPermission;
import com.aioveu.auth.model.vo.SysRolePermissionVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2017/11/17 10:42
 */
@Repository
public interface SysPermissionDao extends BaseMapper<SysPermission> {

    /**
     * 查询权限对应的角色
     * @return
     */
    List<SysRolePermissionVO> getRoleMenuPermission();

}
