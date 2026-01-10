package com.aioveu.pms.aioveu05Sku.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.pms.model.dto.SkuInfoDTO;
import com.aioveu.pms.aioveu05Sku.model.entity.PmsSku;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PmsSkuMapper extends BaseMapper<PmsSku> {

    /**
     * 获取商品库存单元信息
     *
     * @param skuId
     * @return
     */
    SkuInfoDTO getSkuInfo(Long skuId);
}
