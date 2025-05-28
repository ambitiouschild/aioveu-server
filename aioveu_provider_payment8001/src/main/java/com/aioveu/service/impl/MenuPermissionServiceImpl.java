package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.MenuPermissionDao;
import com.aioveu.entity.MenuPermission;
import com.aioveu.exception.SportException;
import com.aioveu.service.MenuPermissionService;
import com.aioveu.service.PermissionService;
import com.aioveu.service.RoleMenuPermissionService;
import com.aioveu.vo.MenuPermissionVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class MenuPermissionServiceImpl extends ServiceImpl<MenuPermissionDao, MenuPermission> implements MenuPermissionService {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleMenuPermissionService roleMenuPermissionService;

    @Override
    public boolean deleteByRoleCode(String menuCode) {
        QueryWrapper<MenuPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MenuPermission::getMenuCode, menuCode);
        // 删除菜单权限关系之前 先删除对应的权限缓存
        roleMenuPermissionService.deleteByMenuCode(menuCode);
        return remove(queryWrapper);
    }

    @Override
    public List<MenuPermission> findByPermissionId(Long permissionId) {
        QueryWrapper<MenuPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MenuPermission::getPermissionId, permissionId);
        return list(queryWrapper);
    }

    @Override
    public IPage<MenuPermissionVo> getByMenuCode(Integer page, Integer size, String menuCode) {
        return getBaseMapper().getByMenuCode(new Page<>(page, size), menuCode);
    }

    @Override
    public boolean addBatch(List<MenuPermission> menuPermissions) {
        List<MenuPermission> menuPermissionList = getBaseMapper().getByMenuAndPermission(menuPermissions);
        if (CollectionUtils.isNotEmpty(menuPermissionList)) {
            throw new SportException("权限重复添加");
        }
        return saveBatch(menuPermissions);
    }

    @Override
    public boolean deleteById(Long id) {
        MenuPermission menuPermission = getById(id);
        roleMenuPermissionService.deleteByMenuCodeAndPermissionId(menuPermission.getMenuCode(), menuPermission.getPermissionId());
        return removeById(id);
    }
}
