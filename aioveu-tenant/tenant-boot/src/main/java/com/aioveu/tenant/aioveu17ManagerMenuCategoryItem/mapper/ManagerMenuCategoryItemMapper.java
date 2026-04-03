package com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.mapper;

import com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.model.entity.ManagerMenuCategoryItem;
import com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.model.query.ManagerMenuCategoryItemQuery;
import com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.model.vo.ManagerMenuCategoryItemVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: ManagerMenuCategoryItemMapper
 * @Description TODO 管理系统工作台菜单项（多租户支持）Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/3 17:26
 * @Version 1.0
 **/
@Mapper
public interface ManagerMenuCategoryItemMapper extends BaseMapper<ManagerMenuCategoryItem> {

    /**
     * 获取管理系统工作台菜单项（多租户支持）分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<ManagerMenuCategoryItemVo>} 管理系统工作台菜单项（多租户支持）分页列表
     */
    Page<ManagerMenuCategoryItemVo> getManagerMenuCategoryItemPage(Page<ManagerMenuCategoryItemVo> page, ManagerMenuCategoryItemQuery queryParams);

}
