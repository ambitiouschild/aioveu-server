package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.dao.RoleMenuDao;
import com.aioveu.entity.RoleMenu;
import com.aioveu.entity.RoleMiniAppMenu;
import com.aioveu.service.RoleMenuPermissionService;
import com.aioveu.service.RoleMenuService;
import com.aioveu.vo.RoleMenuPermissionVO;
import com.aioveu.vo.RoleMenuVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author： yao
 * @Date： 2024/10/28 14:29
 * @Describe：
 */
@Slf4j
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuDao, RoleMenu> implements RoleMenuService {

    @Autowired
    private RoleMenuPermissionService roleMenuPermissionService;

    public static final String PERMISSION_LINK = "@";

    @Override
    public List<RoleMenu> getListByRoleCode(String roleCode) {
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RoleMenu::getRoleCode, roleCode);
        queryWrapper.lambda().eq(RoleMenu::getStatus, 1);
        return list(queryWrapper);
    }

    @Override
    public List<RoleMiniAppMenu> getRoleMenus(List<String> roleList) {
        return this.baseMapper.getRoleMenus(roleList);
    }

    @Override
    public boolean deleteByRoleCode(String roleCode) {
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RoleMenu::getRoleCode, roleCode);
        return remove(queryWrapper);
    }

    @Override
    public List<RoleMenu> getByMenuCode(String menuCode) {
        LambdaQueryWrapper<RoleMenu> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(RoleMenu::getMenuCode, menuCode);
        return list(wrapper);
    }

    @Override
    public Map<String, Object> getRoleMenusByRoleCodeAndType(String roleCode, Integer type) {
        Map<String, Object> menuMap = new HashMap<>();
        List<RoleMenuVO> roleMenuList = getBaseMapper().getRoleMenusByRoleCodeAndParentCode(roleCode, type);
        if (CollectionUtils.isNotEmpty(roleMenuList)) {
            // 查询角色对应的权限
            Map<String, List<RoleMenuPermissionVO>> permissionMap = roleMenuPermissionService.getByRoleCode(roleCode);
            List<RoleMenuVO> resultMenuList = getByParentCode(null, roleMenuList, permissionMap);
            menuMap.put("menuList", resultMenuList);
            Set<String> menuCodeSet = new HashSet<>();
            for (RoleMenuVO roleMenuVO : roleMenuList) {
                if (roleMenuVO.getRoleMenuId() != null) {
                    menuCodeSet.add(roleMenuVO.getCode());
                }
            }
            // 权限选中的数据添加
            for (List<RoleMenuPermissionVO> permissionList : permissionMap.values()) {
                for (RoleMenuPermissionVO roleMenuPermissionVO : permissionList) {
                    if (roleMenuPermissionVO.getRoleMenuPermissionId() != null) {
                        menuCodeSet.add(roleMenuPermissionVO.getMenuCode() + PERMISSION_LINK + roleMenuPermissionVO.getId());
                    }
                }
            }
            Set<String> parentCodeSet = new HashSet<>();
            getParentMenuCode(resultMenuList, parentCodeSet);
            // 将选中的菜单id 移除掉父菜单id
            menuCodeSet.removeAll(parentCodeSet);
            menuMap.put("menuCodeList", menuCodeSet);
        } else {
            menuMap.put("menuList", new ArrayList<>());
            menuMap.put("menuCodeList", new ArrayList<>());
        }
        return menuMap;
    }

    @Override
    public List<RoleMenuVO> getMiniAppMenuByRoleCode(String roleCode) {
        // 先查找当前用户的菜单和权限
        List<RoleMenuVO> roleMenuList = getBaseMapper().getRoleMenusByRoleCode(OauthUtils.getCurrentUserRoles(), 0);
        if (CollectionUtils.isNotEmpty(roleMenuList)) {
            for (RoleMenuVO roleMenuVO : roleMenuList) {
                roleMenuVO.setRoleMenuId(null);
            }
            // 查询角色对应的权限
            Map<String, List<RoleMenuPermissionVO>> permissionMap = roleMenuPermissionService.getByRoleCode(roleCode);
            // 再查找当前角色选中的菜单和权限
            if (StringUtils.isNotEmpty(roleCode)) {
                List<RoleMenuVO> userRoleMenuList = getBaseMapper().getRoleMenusByRoleCode(Collections.singletonList(roleCode), 0);
                if (CollectionUtils.isNotEmpty(userRoleMenuList)) {
                    for (RoleMenuVO roleMenuVO : roleMenuList) {
                        for (RoleMenuVO item : userRoleMenuList) {
                            if (roleMenuVO.getCode().equals(item.getCode())) {
                                roleMenuVO.setRoleMenuId(item.getRoleMenuId());
                            }
                        }
                    }
                }
            }
            return getByParentCode(null, roleMenuList, permissionMap);
        }
        return Collections.emptyList();
    }

    /**
     * 角色权限转成菜单树形结构
     * @param permissionList
     * @param menuCode
     * @return
     */
    private List<RoleMenuVO> permission2RoleMenu(List<RoleMenuPermissionVO> permissionList, String menuCode) {
        return permissionList.stream().map(permission -> {
            RoleMenuVO roleMenuVO = new RoleMenuVO();
            roleMenuVO.setId(permission.getId());
            roleMenuVO.setRoleMenuId(permission.getRoleMenuPermissionId());
            roleMenuVO.setName(permission.getName());
            roleMenuVO.setCode(menuCode + PERMISSION_LINK + permission.getId());
            roleMenuVO.setChildren(new ArrayList<>());
            roleMenuVO.setChecked(roleMenuVO.getRoleMenuId() == null ? 0 : 1);
            return roleMenuVO;
        }).collect(Collectors.toList());
    }

    private List<RoleMenuVO> getByParentCode(String parentCode, List<RoleMenuVO> roleMenuList, Map<String, List<RoleMenuPermissionVO>> permissionMap) {
        List<RoleMenuVO> resultList = new ArrayList<>();
        for (RoleMenuVO roleMenuVO : roleMenuList) {
            List<RoleMenuVO> children;
            if (parentCode == null) {
                if (roleMenuVO.getParentCode() == null) {
                    children = getByParentCode(roleMenuVO.getCode(), roleMenuList, permissionMap);
                    if (permissionMap.get(roleMenuVO.getCode()) != null) {
                        List<RoleMenuVO> permissionChildren = permission2RoleMenu(permissionMap.get(roleMenuVO.getCode()), roleMenuVO.getCode());
                        children.addAll(permissionChildren);
                    }
                    roleMenuVO.setChildren(children);
                    if (CollectionUtils.isNotEmpty(children)) {
                        // 遍历children 判断父级菜单是否全选中 还是半选中 还是未选中
                        roleMenuVO.setChecked(getParentCheckedStatus(children));
                    } else {
                        roleMenuVO.setChecked(roleMenuVO.getRoleMenuId() == null ? 0 : 1);
                    }
                    resultList.add(roleMenuVO);
                }
            } else if (parentCode.equals(roleMenuVO.getParentCode() + "")) {
                children = getByParentCode(roleMenuVO.getCode(), roleMenuList, permissionMap);
                if (permissionMap.get(roleMenuVO.getCode()) != null) {
                    List<RoleMenuVO> permissionChildren = permission2RoleMenu(permissionMap.get(roleMenuVO.getCode()), roleMenuVO.getCode());
                    children.addAll(permissionChildren);
                }
                roleMenuVO.setChildren(children);
                if (CollectionUtils.isNotEmpty(children)) {
                    // 遍历children 判断父级菜单是否全选中 还是半选中 还是未选中
                    roleMenuVO.setChecked(getParentCheckedStatus(children));
                } else {
                    roleMenuVO.setChecked(roleMenuVO.getRoleMenuId() == null ? 0 : 1);
                }

                resultList.add(roleMenuVO);
            }
        }
        return resultList;
    }

    /**
     * 根据子级菜单的选中情况 返回父级菜单的选中情况
     * @param children
     * @return
     */
    private int getParentCheckedStatus(List<RoleMenuVO> children) {
        if (CollectionUtils.isNotEmpty(children)) {
            boolean allChecked = true;
            boolean halfChecked = false;
            for (RoleMenuVO roleMenuVO : children) {
                if (roleMenuVO.getChecked() == 1) {
                    // 有菜单选中 那状态至少是半选中
                    halfChecked = true;
                } else if (roleMenuVO.getChecked() == 0) {
                    // 有菜单未选中 那状态肯定不是全选中
                    allChecked = false;
                    if (halfChecked) {
                        // 如果已经有选中的 那么直接中断循环
                        break;
                    }
                }
            }
            if (allChecked) {
                return 1;
            } else if (halfChecked) {
                return -1;
            }
            return 0;
        }
        return 0;
    }

    /**
     * 查找出菜单的父级Id
     * @param roleMenuList
     * @param parentIdSet
     */
    private void getParentMenuCode(List<RoleMenuVO> roleMenuList, Set<String> parentIdSet) {
        // 找出所有父菜单的id
        for (RoleMenuVO roleMenuVO : roleMenuList) {
            List<RoleMenuVO> children = roleMenuVO.getChildren();
            if (CollectionUtils.isNotEmpty(children)) {
                parentIdSet.add(roleMenuVO.getCode());
                getParentMenuCode(children, parentIdSet);
            }
        }
    }

}
