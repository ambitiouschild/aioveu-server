
package com.aioveu.oms.converter;

import com.aioveu.oms.model.dto.CartItemDto;
import com.aioveu.pms.model.dto.SkuInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @Description: TODO 购物车对象转化器
 * @Author: 雒世松
 * @Date: 2025/6/5 18:07
 * @param
 * @return:
 **/

@Mapper(componentModel = "spring")
public interface CartConverter {

    @Mappings({
            @Mapping(target = "skuId", source = "id"),
    })
    CartItemDto sku2CartItem(SkuInfoDTO skuInfo);

}