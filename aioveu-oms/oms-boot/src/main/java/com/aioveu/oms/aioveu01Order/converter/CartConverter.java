
package com.aioveu.oms.aioveu01Order.converter;

import com.aioveu.oms.aioveu01Order.model.vo.CartItemDto;
import com.aioveu.pms.model.dto.SkuInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @Description: TODO 购物车对象转化器
 *                      这是一个使用 MapStruct 框架的转换器接口，不需要手动编写实现类，MapStruct 会在编译时自动生成实现类。
 *                      MapStruct 工作原理
 *                            1. 自动生成的实现类位置
 *                            MapStruct 在编译时会在 target/generated-sources/annotations/目录下自动生成实现类：
 *                            3. 项目依赖配置
 *                              要使MapStruct正常工作，需要在 pom.xml中添加依赖：
 *                            问题： 字段映射不生效
 *                            解决： 检查字段名和类型是否匹配，或使用@Mapping注解显式指定
 * @Author: 雒世松
 * @Date: 2025/6/5 18:07
 * @param
 * @return:
 **/

@Mapper(componentModel = "spring")
public interface CartConverter {

    @Mappings({
            @Mapping(target = "skuId", source = "id")
    })
    CartItemDto sku2CartItem(SkuInfoDTO skuInfo);

}