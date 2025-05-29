package com.aioveu.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.auth.dao.SysPermissionDao;
import com.aioveu.auth.model.entity.SysPermission;
import com.aioveu.auth.model.vo.SysRolePermissionVO;
import com.aioveu.auth.service.SysPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2020/11/17 10:42
 */
@Slf4j
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionDao, SysPermission> implements SysPermissionService {

    @Override
    public List<SysRolePermissionVO> listRolePermission() {
        List<SysRolePermissionVO> sysRolePermissionList = getBaseMapper().getRoleMenuPermission();
        for (SysRolePermissionVO sysRolePermission : sysRolePermissionList) {
            sysRolePermission.setUrl(sysRolePermission.getMethod() + ":" + sysRolePermission.getUrl());
            String[] strArray = sysRolePermission.getRoleCodeStr().split(",");
            ArrayList< String> roleList = new ArrayList<>(strArray.length);
            Collections.addAll(roleList, strArray);
            sysRolePermission.setRoles(roleList);
        }
        return sysRolePermissionList;
    }

    @Override
    public List<String> getWhiteListUrl() {
        QueryWrapper<SysPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysPermission::getNeedToken, false);
        return list(queryWrapper).stream().map(SysPermission::getUrl).collect(Collectors.toList());
    }
}
