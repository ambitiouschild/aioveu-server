package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.RoleMiniAppMenu;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface RoleMiniAppMenuService extends IService<RoleMiniAppMenu> {

    /**
     * 查询当前用户对应店铺的菜单权限
     * @param storeId
     * @return
     */
    List<RoleMiniAppMenu> getByUserRole(Long storeId);

}
