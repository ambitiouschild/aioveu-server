package com.aioveu.auth.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.auth.common.model.TokenConstant;
import com.aioveu.auth.dao.SysUserRoleDao;
import com.aioveu.auth.model.po.SysUserRole;
import com.aioveu.auth.service.SysUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2020/11/17 10:42
 */
@Slf4j
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleDao, SysUserRole> implements SysUserRoleService {

    @Override
    public List<SysUserRole> findByUserId(String userId) {
        QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUserRole::getUserId, userId);
        List<SysUserRole> list = this.list(queryWrapper);
        if (CollectionUtil.isEmpty(list)) {
            // 对于没有权限的用户，默认统一给普通用户角色
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleCode(TokenConstant.NORMAL_USER_ROLE_CODE);
            return Collections.singletonList(sysUserRole);
        } else if (list.stream().noneMatch(item -> item.getRoleCode().equalsIgnoreCase(TokenConstant.NORMAL_USER_ROLE_CODE))){
            // 对于无普通角色的用户添加普通角色
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleCode(TokenConstant.NORMAL_USER_ROLE_CODE);
            list.add(sysUserRole);
        }
        return list;
    }
}
