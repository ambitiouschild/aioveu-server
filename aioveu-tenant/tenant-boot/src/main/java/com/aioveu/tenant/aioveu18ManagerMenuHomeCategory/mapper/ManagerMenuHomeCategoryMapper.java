package com.aioveu.tenant.aioveu18ManagerMenuHomeCategory.mapper;

import com.aioveu.tenant.aioveu18ManagerMenuHomeCategory.model.entity.ManagerMenuHomeCategory;
import com.aioveu.tenant.aioveu18ManagerMenuHomeCategory.model.query.ManagerMenuHomeCategoryQuery;
import com.aioveu.tenant.aioveu18ManagerMenuHomeCategory.model.vo.ManagerMenuHomeCategoryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: ManagerMenuHomeCategoryMapper
 * @Description TODO 管理端app首页分类配置Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/4 13:39
 * @Version 1.0
 **/
@Mapper
public interface ManagerMenuHomeCategoryMapper extends BaseMapper<ManagerMenuHomeCategory> {

    /**
     * 获取管理端app首页分类配置分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<ManagerMenuHomeCategoryVo>} 管理端app首页分类配置分页列表
     */
    Page<ManagerMenuHomeCategoryVo> getManagerMenuHomeCategoryPage(Page<ManagerMenuHomeCategoryVo> page, ManagerMenuHomeCategoryQuery queryParams);

}
