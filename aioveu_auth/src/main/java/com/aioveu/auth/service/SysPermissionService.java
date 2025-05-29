package com.aioveu.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.auth.model.entity.SysPermission;
import com.aioveu.auth.model.vo.SysRolePermissionVO;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2020/11/17 10:42
 */
public interface SysPermissionService extends IService<SysPermission> {

    /**
     * 获取所有的url->角色对应关系
     * @return
     */
    List<SysRolePermissionVO> listRolePermission();

    /**
     * 获取白名单
     * @return
     */
    List<String> getWhiteListUrl();

}
