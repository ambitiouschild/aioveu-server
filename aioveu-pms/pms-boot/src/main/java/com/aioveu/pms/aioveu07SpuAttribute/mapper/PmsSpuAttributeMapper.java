package com.aioveu.pms.aioveu07SpuAttribute.mapper;

import com.aioveu.pms.aioveu07SpuAttribute.model.query.PmsSpuAttributeQuery;
import com.aioveu.pms.aioveu07SpuAttribute.model.vo.PmsSpuAttributeVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.pms.aioveu07SpuAttribute.model.entity.PmsSpuAttribute;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO 商品类型（属性/规格）Mapper接口
 * @Date  2026/1/11 22:06
 * @Param
 * @return
 **/

@Mapper
public interface PmsSpuAttributeMapper extends BaseMapper<PmsSpuAttribute> {

    /**
     * 获取商品类型（属性/规格）分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<PmsSpuAttributeVO>} 商品类型（属性/规格）分页列表
     */
    Page<PmsSpuAttributeVO> getPmsSpuAttributePage(Page<PmsSpuAttributeVO> page, PmsSpuAttributeQuery queryParams);

}
