package com.aioveu.pms.aioveu05Sku.mapper;

import com.aioveu.pms.aioveu05Sku.model.query.PmsSkuQuery;
import com.aioveu.pms.aioveu05Sku.model.vo.PmsSkuVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.pms.model.dto.SkuInfoDTO;
import com.aioveu.pms.aioveu05Sku.model.entity.PmsSku;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO 商品库存Mapper接口
 * @Date  2026/1/11 21:03
 * @Param
 * @return
 **/

@Mapper
public interface PmsSkuMapper extends BaseMapper<PmsSku> {

    /**
     * 获取商品库存单元信息
     *
     * @param skuId
     * @return
     */
    SkuInfoDTO getSkuInfo(Long skuId);

    /**
     * 获取商品库存分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<PmsSkuVO>} 商品库存分页列表
     */
    Page<PmsSkuVO> getPmsSkuPage(Page<PmsSkuVO> page, PmsSkuQuery queryParams);
}
