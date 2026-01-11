package com.aioveu.pms.aioveu04CategoryBrand.mapper;

import com.aioveu.pms.aioveu04CategoryBrand.model.query.PmsCategoryBrandQuery;
import com.aioveu.pms.aioveu04CategoryBrand.model.vo.PmsCategoryBrandVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.pms.aioveu04CategoryBrand.model.entity.PmsCategoryBrand;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO 商品分类与品牌关联表Mapper接口
 * @Date  2026/1/11 20:06
 * @Param
 * @return
 **/


@Mapper
public interface PmsCategoryBrandMapper extends BaseMapper<PmsCategoryBrand> {

    /**
     * 获取商品分类与品牌关联表分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<PmsCategoryBrandVO>} 商品分类与品牌关联表分页列表
     */
    Page<PmsCategoryBrandVO> getPmsCategoryBrandPage(Page<PmsCategoryBrandVO> page, PmsCategoryBrandQuery queryParams);

}
