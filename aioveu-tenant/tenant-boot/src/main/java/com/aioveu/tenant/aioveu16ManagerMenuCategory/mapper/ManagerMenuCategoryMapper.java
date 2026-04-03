package com.aioveu.tenant.aioveu16ManagerMenuCategory.mapper;

import com.aioveu.tenant.aioveu16ManagerMenuCategory.model.entity.ManagerMenuCategory;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.model.query.ManagerMenuCategoryQuery;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.model.vo.ManagerMenuCategoryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;


/**
 * @ClassName: ManagerMenuCategoryMapper
 * @Description TODO 管理端菜单分类（多租户）Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/3 17:11
 * @Version 1.0
 **/
@Mapper
public interface ManagerMenuCategoryMapper  extends BaseMapper<ManagerMenuCategory> {

    /**
     * 获取管理端菜单分类（多租户）分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<ManagerMenuCategoryVo>} 管理端菜单分类（多租户）分页列表
     */
    Page<ManagerMenuCategoryVo> getManagerMenuCategoryPage(Page<ManagerMenuCategoryVo> page, ManagerMenuCategoryQuery queryParams);
}
