package com.aioveu.pms.aioveu05Sku.converter;

import com.aioveu.pms.model.dto.SkuInfoDTO;
import com.aioveu.pms.aioveu05Sku.model.entity.PmsSku;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @Description: TODO 商品对象转换器
 * @Author: 雒世松
 * @Date: 2025/6/5 18:29
 * @param
 * @return:
 **/

@Mapper(componentModel = "spring")
public interface SkuConverter {

    @Mappings({
            @Mapping(target = "skuName", source = "name")
    })
    SkuInfoDTO entity2SkuInfoDto(PmsSku entity);

    List<SkuInfoDTO> entity2SkuInfoDto(List<PmsSku> list);
}
