package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.Menu;

import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface MenuService extends IService<Menu> {

    /**
     * 获取角色对应的web端用户菜单
     * @param roleCodes
     * @return
     */
    List<Map<String, Object>> getWebMenuByRoleCode(List<String> roleCodes);

    /**
     * web端菜单列表
     * @param page
     * @param size
     * @param type
     * @param parentCode
     * @param keyword
     * @return
     */
    IPage<Menu> getWebList(Integer page, Integer size, Integer type, String parentCode, String keyword);

    /**
     * 根据菜单code删除
     * @param code
     * @return
     */
    boolean deleteById(String code);

    /**
     * 保存或者更新
     * @param menu
     * @return
     */
    boolean saveOrUpdateMenu(Menu menu);



}
