package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.MenuDao;
import com.aioveu.entity.Menu;
import com.aioveu.entity.RoleMenu;
import com.aioveu.enums.DataStatus;
import com.aioveu.exception.SportException;
import com.aioveu.service.MenuPermissionService;
import com.aioveu.service.MenuService;
import com.aioveu.service.RoleMenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class MenuServiceImpl extends ServiceImpl<MenuDao, Menu> implements MenuService {

    @Autowired
    private MenuPermissionService menuPermissionService;

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<Map<String, Object>> getWebMenuByRoleCode(List<String> roleCodes) {
        if (CollectionUtils.isNotEmpty(roleCodes)) {
            List<Menu> menuList = getBaseMapper().getWebMenuByRoleCode(roleCodes);
            if (CollectionUtils.isNotEmpty(menuList)) {
                return getRouterByCode(null, menuList);
            }
        }
        return Collections.emptyList();
    }

    @Override
    public IPage<Menu> getWebList(Integer page, Integer size, Integer type, String parentCode, String keyword) {
        LambdaQueryWrapper<Menu> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Menu::getType, type)
                .eq(Menu::getStatus, DataStatus.NORMAL.getCode());
        if (StringUtils.isNotEmpty(parentCode)) {
            wrapper.eq(Menu::getParentCode, parentCode);
        } else {
            wrapper.isNull(Menu::getParentCode);
        }
        if (StringUtils.isNotEmpty(keyword)) {
            wrapper.like(Menu::getName, keyword)
                    .or().like(Menu::getCode, keyword);
        }
        wrapper.orderByAsc(Menu::getPriority);
        return getBaseMapper().selectPage(new Page<>(page, size), wrapper);
    }

    @Override
    public boolean deleteById(String code) {
        List<RoleMenu> roleMenuList = roleMenuService.getByMenuCode(code);
        if (CollectionUtils.isNotEmpty(roleMenuList)) {
            throw new SportException("该菜单已分配角色，不能删除！");
        }
        if (remove(new QueryWrapper<Menu>().lambda().eq(Menu::getCode, code))) {
            menuPermissionService.deleteByRoleCode(code);
            return true;
        }
        throw new SportException("操作失败");
    }

    @Override
    public boolean saveOrUpdateMenu(Menu menu) {
        LambdaQueryWrapper<Menu> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Menu::getCode, menu.getCode());
        if (menu.getId() == null && count(wrapper) > 0) {
            throw new SportException("菜单编号:" + menu.getCode() + "已存在");
        }
        if (menu.getType() == 0) {

        } else if (menu.getType() == 2) {
            if (StringUtils.isEmpty(menu.getComponent())) {
                throw new SportException("组件名称不能为空");
            }
            if (menu.getMeta() == null) {
                throw new SportException("路由元信息不能为空");
            }
            if (StringUtils.isEmpty(menu.getPath())) {
                throw new SportException("路径不能为空");
            }
        }
        return saveOrUpdate(menu);
    }

    public List<Map<String, Object>> getRouterByCode(String parentCode, List<Menu> menuList) {
        List<Map<String, Object>> routerList = new ArrayList<>();
        for (Menu menu : menuList) {
            if (parentCode == null) {
                if (StringUtils.isEmpty(menu.getParentCode())) {
                    routerList.add(getRouter(menu, menuList));
                }
            } else {
                if (parentCode.equals(menu.getParentCode() + "")) {
                    routerList.add(getRouter(menu, menuList));
                }
            }
        }
        // 使用 Stream API 对菜单进行排序
        return routerList.stream()
                .sorted(Comparator.comparingInt(o -> (Integer) o.get("priority")))
                .collect(Collectors.toList());
    }

    /**
     * 获取具体菜单
     * @param menu
     * @param menuList
     * @return
     */
    private Map<String, Object> getRouter(Menu menu, List<Menu> menuList) {
        Map<String, Object> router = new HashMap<>();
        router.put("path", menu.getPath());
        router.put("component", menu.getComponent());
        router.put("name", menu.getName());
        router.put("meta", menu.getMeta());
        router.put("priority", menu.getPriority());
        if (StringUtils.isNotEmpty(menu.getRedirect())) {
            router.put("redirect", menu.getRedirect());
        }
        if (menu.getHidden()) {
            router.put("hidden", true);
        }
        List<Map<String, Object>> children = getRouterByCode(menu.getCode(), menuList);
        router.put("children", children);
        return router;
    }

}
