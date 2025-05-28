package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.auth.common.model.SysConstant;
import com.aioveu.dao.PermissionDao;
import com.aioveu.entity.MenuPermission;
import com.aioveu.entity.Permission;
import com.aioveu.exception.SportException;
import com.aioveu.service.MenuPermissionService;
import com.aioveu.service.PermissionService;
import com.aioveu.service.RoleMenuPermissionService;
import com.aioveu.vo.RolePermissionVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionDao, Permission> implements PermissionService {

    @Autowired
    private MenuPermissionService menuPermissionService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private RoleMenuPermissionService roleMenuPermissionService;

    @Override
    public RolePermissionVO getRolePermissionById(Long id) {
        return getBaseMapper().getById(id);
    }

    @Override
    public Long create(Permission permission) {
        LambdaQueryWrapper<Permission> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Permission::getUrl, permission.getUrl());
        wrapper.eq(Permission::getMethod, permission.getMethod());
        if (count(wrapper) > 0) {
            throw new SportException("url已存在, 请勿重复添加");
        }
        if (save(permission)) {
            // 新增权限不需要认证 同步到缓存中
            if (!permission.getNeedToken()) {
                redisTemplate.opsForList().rightPush(SysConstant.URL_WHITELIST, permission.getUrl());
            }
            return permission.getId();
        }
        return null;
    }

    @Override
    public IPage<Permission> getList(Integer page, Integer size, String keyword, Boolean needToken) {
        LambdaQueryWrapper<Permission> wrapper = Wrappers.lambdaQuery();
        if (needToken != null) {
            wrapper.eq(Permission::getNeedToken, needToken);
        }
        if (StringUtils.isNotEmpty(keyword)) {
            wrapper.like(Permission::getName, keyword)
            .or().like(Permission::getUrl, keyword);
        }
        wrapper.orderByDesc(Permission::getCreateDate);
        return getBaseMapper().selectPage(new Page<>(page, size), wrapper);
    }

    @Override
    public boolean deleteById(Long id) {
        List<MenuPermission> menuPermissions = menuPermissionService.findByPermissionId(id);
        if (CollectionUtils.isNotEmpty(menuPermissions)) {
            List<Long> menuPermissionIds = menuPermissions.stream().map(MenuPermission::getId).collect(Collectors.toList());
            // 删除该权限引用的所有菜单关系
            menuPermissionService.removeByIds(menuPermissionIds);
            // 删除角色引用的所有权限
            roleMenuPermissionService.deleteByPermissionId(id);
        }
        Permission permission = getById(id);
        if (permission != null && !permission.getNeedToken()) {
            // 删除缓存中非认证接口
            redisTemplate.opsForList().remove(SysConstant.URL_WHITELIST, 0, permission.getUrl());
        }
        return removeById(id);
    }

    @Override
    public boolean updatePermission(Permission permission) {
        Permission old = getById(permission.getId());
        if (!old.getNeedToken()) {
            redisTemplate.opsForList().remove(SysConstant.URL_WHITELIST, 0, old.getUrl());
        }
        if (!permission.getNeedToken()) {
            redisTemplate.opsForList().rightPush(SysConstant.URL_WHITELIST, permission.getUrl());
        }
        return updateById(permission);
    }
}
