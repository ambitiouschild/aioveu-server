package com.aioveu.system.aioveu04Menu.mapper;

import com.aioveu.system.aioveu04Menu.model.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

/**
 * @ClassName: MenuMapper
 * @Description TODO  菜单访问层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 18:03
 * @Version 1.0
 **/

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 获取菜单路由列表
     *
     * @param roleCodes 角色编码集合
     */
    List<Menu> getMenusByRoleCodes(Set<String> roleCodes);
}
