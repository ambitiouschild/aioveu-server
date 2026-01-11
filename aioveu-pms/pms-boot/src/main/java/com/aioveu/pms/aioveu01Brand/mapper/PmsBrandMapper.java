package com.aioveu.pms.aioveu01Brand.mapper;

import com.aioveu.pms.aioveu01Brand.model.query.PmsBrandQuery;
import com.aioveu.pms.aioveu01Brand.model.vo.PmsBrandVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.pms.aioveu01Brand.model.entity.PmsBrand;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO 商品品牌Mapper接口
 * @Date  2026/1/10 19:03
 * @Param
 * @return
 **/


@Mapper
public interface PmsBrandMapper extends BaseMapper<PmsBrand> {

    /**
     * 获取商品品牌分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<PmsBrandVO>} 商品品牌分页列表
     */
    Page<PmsBrandVO> getPmsBrandPage(Page<PmsBrandVO> page, PmsBrandQuery queryParams);

}
