package com.aioveu.pms.aioveu07SpuAttribute.converter;

import com.aioveu.pms.aioveu07SpuAttribute.model.entity.PmsSpuAttribute;
import com.aioveu.pms.aioveu07SpuAttribute.model.form.PmsSpuAttributeForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @Description: TODO 商品类型（属性/规格）对象转换器
 * @Author: 雒世松
 * @Date: 2025/6/5 18:29
 * @param
 * @return:
 **/

@Mapper(componentModel = "spring")
public interface PmsSpuAttributeConverter {

    @Mappings({
            @Mapping(target = "id",ignore = true)
    })
    PmsSpuAttribute form2Entity(PmsSpuAttributeForm form);

    PmsSpuAttributeForm toForm(PmsSpuAttribute entity);

    PmsSpuAttribute toEntity(PmsSpuAttributeForm formData);

}
