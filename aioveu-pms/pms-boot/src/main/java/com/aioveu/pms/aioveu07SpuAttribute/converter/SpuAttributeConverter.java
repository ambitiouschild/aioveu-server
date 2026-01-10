package com.aioveu.pms.aioveu07SpuAttribute.converter;

import com.aioveu.pms.aioveu07SpuAttribute.model.entity.PmsSpuAttribute;
import com.aioveu.pms.aioveu07SpuAttribute.model.form.PmsSpuAttributeForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @Description: TODO 商品属性对象转换器
 * @Author: 雒世松
 * @Date: 2025/6/5 18:29
 * @param
 * @return:
 **/

@Mapper(componentModel = "spring")
public interface SpuAttributeConverter {

    @Mappings({
            @Mapping(target = "id",ignore = true)
    })
    PmsSpuAttribute form2Entity(PmsSpuAttributeForm form);

}
