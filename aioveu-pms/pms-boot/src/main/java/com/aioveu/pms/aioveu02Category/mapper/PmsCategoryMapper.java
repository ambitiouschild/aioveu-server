package com.aioveu.pms.aioveu02Category.mapper;

import com.aioveu.pms.aioveu02Category.model.query.PmsCategoryQuery;
import com.aioveu.pms.aioveu02Category.model.vo.PmsCategoryVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.pms.aioveu02Category.model.entity.PmsCategory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;


/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO  商品分类Mapper接口
 * @Date  2026/1/11 17:32
 * @Param
 * @return
 **/
@Mapper
public interface PmsCategoryMapper extends BaseMapper<PmsCategory> {

    /**
     * 获取商品分类分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<PmsCategoryVO>} 商品分类分页列表
     */
    Page<PmsCategoryVO> getPmsCategoryPage(Page<PmsCategoryVO> page, PmsCategoryQuery queryParams);

}
