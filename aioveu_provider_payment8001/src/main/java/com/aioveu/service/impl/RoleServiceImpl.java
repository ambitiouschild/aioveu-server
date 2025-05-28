package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.RoleDao;
import com.aioveu.entity.Role;
import com.aioveu.entity.RoleMenu;
import com.aioveu.entity.RoleMenuPermission;
import com.aioveu.entity.RoleUser;
import com.aioveu.enums.RoleTypeEnum;
import com.aioveu.exception.SportException;
import com.aioveu.service.*;
import com.aioveu.vo.IdNameCodeVO;
import com.aioveu.vo.RoleDetailVO;
import com.aioveu.vo.WebRoleVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements RoleService {

    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private RoleUserService roleUserService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleMenuPermissionService roleMenuPermissionService;

    @Override
    public Role getRoleByCode(String code) {
        return getOne(new QueryWrapper<Role>().eq("status", 1).eq("code", code));
    }

    @Override
    public List<IdNameCodeVO> getAll() {
        return list(new QueryWrapper<Role>()
                .eq("status", 1)
        )
                .stream()
                .map(item -> new IdNameCodeVO(item.getId(), item.getName(), item.getCode()))
                .collect(Collectors.toList());
    }

    @Override
    public List<IdNameCodeVO> manageList(String storeId) {

        return list(new QueryWrapper<Role>()
                .eq("status", 1)
                .eq("type", RoleTypeEnum.THIRD_ROLE.getCode())
                .eq("store_id", storeId)
        )
                .stream()
                .map(item -> new IdNameCodeVO(item.getId(), item.getName(), item.getCode()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Role> getBusinessRoleList(String storeId) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Role::getStatus, 1)
                .and(wrapper -> {
                    wrapper.and(wrapper2 -> wrapper2.eq(Role::getStoreId, storeId).eq(Role::getType, RoleTypeEnum.THIRD_ROLE.getCode()))
                            .or(wrapper2 -> wrapper2.eq(Role::getType, RoleTypeEnum.INTERNAL_ROLE.getCode()));

                });
        return list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateRole(RoleDetailVO roleVO) {
        if (StringUtils.isEmpty(roleVO.getCode())) {
            throw new SportException("角色编号不能为空");
        }
        if (roleVO.getType() == null) {
            throw new SportException("角色类型不能为空");
        }
        if (roleVO.getCompanyId() == null) {
            roleVO.setCompanyId(0L);
        }
        if (roleVO.getStoreId() == null) {
            roleVO.setStoreId(0L);
        }
        Role role = new Role();
        boolean isAdd = roleVO.getId() == null;
        if (isAdd) {
            Role roleByCode = this.getRoleByCode(roleVO.getCode());
            if (roleByCode != null) {
                throw new SportException("角色编号已经存在！");
            }
        }
        BeanUtils.copyProperties(roleVO, role);
        saveOrUpdate(role);
        if (CollectionUtils.isNotEmpty(roleVO.getMenuCodes())) {
            if (!isAdd) {
                roleMenuService.deleteByRoleCode(roleVO.getCode());
            }

            Set<String> menuCodes = new HashSet<>(roleVO.getMenuCodes());

            List<String> roleMenuPermissionList = menuCodes.stream().filter(str -> str.contains("@"))
                    .collect(Collectors.toList());

            List<String> roleMenuCodeList = menuCodes.stream().filter(str -> !str.contains("@"))
                    .collect(Collectors.toList());
            // 保存角色菜单关系
            List<RoleMenu> roleMenuList = roleMenuCodeList.stream().map(menuCode -> {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleCode(roleVO.getCode());
                roleMenu.setMenuCode(menuCode);
                return roleMenu;
            }).collect(Collectors.toList());
            if (roleMenuService.saveBatch(roleMenuList)) {
                // 保存角色菜单权限关系
                if (CollectionUtils.isNotEmpty(roleMenuPermissionList)) {
                    // 查询角色对应权限字典
                    List<RoleMenuPermission> roleMenuPermissions = new ArrayList<>();
                    for (String str : roleMenuPermissionList) {
                        String[] array = str.split(RoleMenuServiceImpl.PERMISSION_LINK);
                        Long permissionId = Long.valueOf(array[1]);
                        String menuCode = array[0];
                        RoleMenuPermission roleMenuPermission = new RoleMenuPermission();
                        roleMenuPermission.setRoleCode(roleVO.getCode());
                        roleMenuPermission.setMenuCode(menuCode);
                        roleMenuPermission.setPermissionId(permissionId);
                        roleMenuPermissions.add(roleMenuPermission);
                    }
                    // 同步删除关系表和缓存中的角色权限
                    roleMenuPermissionService.deleteByRoleCode(roleVO.getCode());
                    roleMenuPermissionService.saveBatch(roleMenuPermissions);
                    //同步新增缓存中的角色权限
                    roleMenuPermissionService.syncRolePermission2Cache(roleMenuPermissions.stream().map(RoleMenuPermission::getId).collect(Collectors.toList()));
                }
            }
        } else {
            // 删除角色对应菜单和权限以及权限缓存
            roleMenuService.deleteByRoleCode(roleVO.getCode());
            roleMenuPermissionService.deleteByRoleCode(roleVO.getCode());
        }
        return true;
    }

    @Override
    public RoleDetailVO getDetail(Long id) {
        Role role = this.getById(id);
        RoleDetailVO roleDetailVO = new RoleDetailVO();
        BeanUtils.copyProperties(role, roleDetailVO);
        List<RoleMenu> roleMenus = roleMenuService.getListByRoleCode(role.getCode());
        roleDetailVO.setMenuCodes(roleMenus.stream().map(RoleMenu::getMenuCode).collect(Collectors.toList()));
        return roleDetailVO;
    }

    @Override
    public IPage<WebRoleVO> webList(Integer page, Integer size, Long storeId, Long companyId, Integer type) {
        return getBaseMapper().getWebList(new Page<>(page, size), storeId, companyId, type);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByCode(String code) {
        List<RoleUser> roleUserList = roleUserService.getByRoleCode(code);
        if (CollectionUtils.isNotEmpty(roleUserList)) {
            throw new SportException("该角色已分配用户，不能删除！");
        }
        if (remove(new QueryWrapper<Role>().lambda().eq(Role::getCode, code)) && roleMenuService.deleteByRoleCode(code)) {
            roleMenuPermissionService.deleteByRoleCode(code);
            return true;
        }
        throw new SportException("操作失败");
    }
}
