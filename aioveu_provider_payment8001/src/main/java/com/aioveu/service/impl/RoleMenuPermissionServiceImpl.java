package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.auth.common.model.SysConstant;
import com.aioveu.dao.RoleMenuPermissionDao;
import com.aioveu.entity.RoleMenuPermission;
import com.aioveu.service.RoleMenuPermissionService;
import com.aioveu.vo.RoleMenuPermissionVO;
import com.aioveu.vo.RolePermissionVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class RoleMenuPermissionServiceImpl extends ServiceImpl<RoleMenuPermissionDao, RoleMenuPermission> implements RoleMenuPermissionService {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public Map<String, List<RoleMenuPermissionVO>> getByRoleCode(String roleCode) {
        List<RoleMenuPermissionVO> list = getBaseMapper().getByRoleCode(roleCode);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.stream().collect(Collectors.groupingBy(RoleMenuPermissionVO::getMenuCode));
        }
        return new HashMap<>();
    }

    @Override
    public Map<Long, List<RoleMenuPermissionVO>> getPermissionMenuMap(String roleCode) {
        List<RoleMenuPermissionVO> list = getBaseMapper().getByRoleCode(roleCode);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.stream().collect(Collectors.groupingBy(RoleMenuPermissionVO::getId));
        }
        return Collections.emptyMap();
    }

    @Override
    public boolean deleteByRoleCode(String roleCode) {
        LambdaQueryWrapper<RoleMenuPermission> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(RoleMenuPermission::getRoleCode, roleCode);
        deletePermissionCache(wrapper);
        return remove(wrapper);
    }

    private void deletePermissionCache(LambdaQueryWrapper<RoleMenuPermission> wrapper) {
        List<RoleMenuPermission> roleMenuPermissionList = list(wrapper);
        if (CollectionUtils.isNotEmpty(roleMenuPermissionList)) {
            //TODO 以下SQL有性能问题 需要优化
            List<RolePermissionVO> rolePermissionList = getBaseMapper().getRolePermissionById(roleMenuPermissionList.stream().map(RoleMenuPermission::getId).collect(Collectors.toList()));
            syncOrRemoveRolePermission(rolePermissionList, true);
        }
    }

    @Override
    public boolean deleteByPermissionId(Long permissionId) {
        LambdaQueryWrapper<RoleMenuPermission> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(RoleMenuPermission::getPermissionId, permissionId);
        deletePermissionCache(wrapper);
        return remove(wrapper);
    }

    @Override
    public boolean deleteByMenuCode(String menuCode) {
        LambdaQueryWrapper<RoleMenuPermission> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(RoleMenuPermission::getMenuCode, menuCode);
        deletePermissionCache(wrapper);
        return remove(wrapper);
    }

    @Override
    public boolean deleteByMenuCodeAndPermissionId(String menuCode, Long permissionId) {
        LambdaQueryWrapper<RoleMenuPermission> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(RoleMenuPermission::getPermissionId, permissionId)
                .eq(RoleMenuPermission::getMenuCode, menuCode);
        deletePermissionCache(wrapper);
        return remove(wrapper);
    }

    @Override
    public boolean syncRolePermission2Cache(List<Long> ids) {
        List<RolePermissionVO> rolePermissionList = getBaseMapper().getRolePermissionById(ids);
        syncOrRemoveRolePermission(rolePermissionList, false);
        return true;
    }

    /**
     * 同步或者删除角色权限到缓存中
     * @param rolePermissionList
     */
    private void syncOrRemoveRolePermission(List<RolePermissionVO> rolePermissionList, boolean remove) {
        for (RolePermissionVO rolePermission : rolePermissionList) {
            String[] strArray = rolePermission.getRoleCodeStr().split(",");
            ArrayList<String> roleOneList = new ArrayList<>(strArray.length);
            Collections.addAll(roleOneList, strArray);
            Set<String> roles = roleOneList.stream().map(item -> SysConstant.ROLE_PREFIX + item).collect(Collectors.toSet());
            List<String> roleList = (List<String>) redisTemplate.opsForHash().get(SysConstant.OAUTH_URLS, rolePermission.getMethod() + ":" + rolePermission.getUrl());
            if (CollectionUtils.isNotEmpty(roleList)) {
                if (remove) {
                    for (String role : roles) {
                        roleList.remove(role);
                    }
                    redisTemplate.opsForHash().put(SysConstant.OAUTH_URLS, rolePermission.getMethod() + ":" + rolePermission.getUrl(), roleList);
                } else {
                    roles.addAll(roleList);
                }
            }
            if (!remove) {
                redisTemplate.opsForHash().put(SysConstant.OAUTH_URLS, rolePermission.getMethod() + ":" + rolePermission.getUrl(), new ArrayList<>(roles));
            }
        }
    }
}
