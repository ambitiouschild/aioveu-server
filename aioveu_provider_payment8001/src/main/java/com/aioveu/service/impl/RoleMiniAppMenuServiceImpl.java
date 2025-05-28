package com.aioveu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.dao.RoleMiniAppMenuDao;
import com.aioveu.entity.RoleMiniAppMenu;
import com.aioveu.enums.MessageType;
import com.aioveu.exception.SportException;
import com.aioveu.service.MessageService;
import com.aioveu.service.RoleMenuService;
import com.aioveu.service.RoleMiniAppMenuService;
import com.aioveu.service.RoleUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class RoleMiniAppMenuServiceImpl extends ServiceImpl<RoleMiniAppMenuDao, RoleMiniAppMenu> implements RoleMiniAppMenuService {

    @Autowired
    private RoleUserService roleUserService;

    @Autowired
    private RoleMenuService roleMenuService;

    private List<RoleMiniAppMenu> reorganizeMenuList(List<RoleMiniAppMenu> list) {
        List<RoleMiniAppMenu> menuList = new ArrayList<>();
        for (RoleMiniAppMenu item : list) {
            RoleMiniAppMenu fatherMenu = null;
            if (item.getParentCode() != null)
                fatherMenu = this.getRoleMiniAppMenuByID(menuList, item.getParentCode());
            if (fatherMenu != null) {
                if (fatherMenu.getRoleMiniAppMenuList() == null) {
                    fatherMenu.setRoleMiniAppMenuList(new ArrayList<>());
                }
                fatherMenu.getRoleMiniAppMenuList().add(item);
            } else
                menuList.add(item);
        }
        return menuList;
    }

    private RoleMiniAppMenu getRoleMiniAppMenuByID(List<RoleMiniAppMenu> menuList, String powerID) {
        RoleMiniAppMenu currentMenu = menuList.stream().filter(x -> x.getCode().equals(powerID)).findAny().orElse(null);
        if (currentMenu == null) {
            for (RoleMiniAppMenu menu : menuList) {
                if (CollectionUtils.isEmpty(menu.getRoleMiniAppMenuList()))
                    continue;
                currentMenu = getRoleMiniAppMenuByID(menu.getRoleMiniAppMenuList(), powerID);
                if (currentMenu != null)
                    break;
            }
        }
        return currentMenu;
    }

    @Autowired
    private MessageService messageService;

    @Override
    public List<RoleMiniAppMenu> getByUserRole(Long storeId) {
        List<String> currentUserRoles = OauthUtils.getCurrentUserRoles();
        if (CollectionUtils.isEmpty(currentUserRoles)) {
            throw new SportException("当前用户未分配角色权限, 请联系后台管理员");
        }
        List<RoleMiniAppMenu> roleMiniAppMenuList = roleMenuService.getRoleMenus(currentUserRoles);
        int unread = 0;
        for (RoleMiniAppMenu item : roleMiniAppMenuList) {
            if (item.getCode().equals("operate-message")) {
                // 判断是否包含经营提醒 菜单权限
                int unreadMessageNumber = messageService.getStoreUnreadMessageNumber(storeId, MessageType.OperateMessage.getCode());
                item.setUnread(unreadMessageNumber);
                unread += unreadMessageNumber;
            } else if (item.getCode().equals("message")) {
                // 判断是否包含系统消息 菜单权限
                int unreadMessageNumber = messageService.getStoreUnreadMessageNumber(storeId, MessageType.SystemMessage.getCode());
                item.setUnread(unreadMessageNumber);
                unread += unreadMessageNumber;
            }
        }
        for (RoleMiniAppMenu item : roleMiniAppMenuList) {
            if (item.getCode().equals("all-message")) {
                item.setUnread(unread);
                break;
            }
        }
        return reorganizeMenuList(roleMiniAppMenuList);
    }
}
