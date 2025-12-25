package com.aioveu.system.mapper;

import com.aioveu.system.model.bo.RouteBO;
import com.aioveu.system.model.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description: TODO 菜单持久接口层
 * @Author: 雒世松
 * @Date: 2025/6/5 17:16
 * @param
 * @return:
 **/

@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<RouteBO> listRoutes();
}
