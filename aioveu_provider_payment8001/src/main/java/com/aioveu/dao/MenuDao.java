package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.entity.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface MenuDao extends BaseMapper<Menu> {

    /**
     * 通过角色查找菜单
     * @param roleCodes
     * @return
     */
    List<Menu> getWebMenuByRoleCode(List<String> roleCodes);


}
