package com.aioveu.pms.aioveu03CategoryAttribute.mapper;

import com.aioveu.pms.aioveu03CategoryAttribute.model.query.PmsCategoryAttributeQuery;
import com.aioveu.pms.aioveu03CategoryAttribute.model.vo.PmsCategoryAttributeVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.pms.aioveu03CategoryAttribute.model.entity.PmsCategoryAttribute;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;


/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO 商品分类类型（规格，属性）Mapper接口
 * @Date  2026/1/11 19:47
 * @Param
 * @return
 **/
@Mapper
public interface PmsCategoryAttributeMapper extends BaseMapper<PmsCategoryAttribute> {


    /**
     * 获取商品类型（规格，属性）分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<PmsCategoryAttributeVO>} 商品类型（规格，属性）分页列表
     */
    Page<PmsCategoryAttributeVO> getPmsCategoryAttributePage(Page<PmsCategoryAttributeVO> page, PmsCategoryAttributeQuery queryParams);
}
