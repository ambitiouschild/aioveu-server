package com.aioveu.tenant.aioveu04Menu.service;

import com.aioveu.common.model.Option;
import com.aioveu.tenant.aioveu04Menu.model.entity.Menu;
import com.aioveu.tenant.aioveu04Menu.model.form.MenuForm;
import com.aioveu.tenant.aioveu04Menu.model.query.MenuQuery;
import com.aioveu.tenant.aioveu04Menu.model.vo.MenuVO;
import com.aioveu.tenant.aioveu04Menu.model.vo.RouteVO;
import com.aioveu.tenant.aioveu11Codegen.model.entity.GenTable;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @ClassName: MenuService
 * @Description TODO 菜单业务接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 21:50
 * @Version 1.0
 **/
public interface MenuService extends IService<Menu> {

    /**
     * 获取菜单表格列表
     */
    List<MenuVO> listMenus(MenuQuery queryParams);

    /**
     * 获取菜单下拉列表
     *
     * @param onlyParent 是否只查询父级菜单
     * @param scope 菜单范围(1=平台 2=租户)
     */
    List<Option<Long>> listMenuOptions(boolean onlyParent, Integer scope);

    /**
     * 新增菜单
     *
     * @param menuForm  菜单表单对象
     */
    boolean saveMenu(MenuForm menuForm);

    /**
     * 获取当前用户的菜单路由列表
     */
    List<RouteVO> listCurrentUserRoutes();

    /**
     * 修改菜单显示状态
     *
     * @param menuId 菜单ID
     * @param visible 是否显示(1-显示 0-隐藏)
     */
    boolean updateMenuVisible(Long menuId, Integer visible);

    /**
     * 获取菜单表单数据
     *
     * @param id 菜单ID
     */
    MenuForm getMenuForm(Long id);

    /**
     * 删除菜单
     *
     * @param id 菜单ID
     */
    boolean deleteMenu(Long id);

    /**
     * 代码生成时添加菜单
     *
     * @param parentMenuId 父菜单ID
     * @param genTable   实体名
     */
    void addMenuForCodegen(Long parentMenuId, GenTable genTable);
}
