package com.aioveu.pms.aioveu03CategoryAttribute.converter;

import com.aioveu.pms.aioveu03CategoryAttribute.model.entity.PmsCategoryAttribute;
import com.aioveu.pms.aioveu03CategoryAttribute.model.form.PmsCategoryAttributeForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: PmsCategoryAttributeConverter
 * @Description TODO 商品分类类型（规格，属性）对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/11 19:47
 * @Version 1.0
 **/

@Mapper(componentModel = "spring")
public interface PmsCategoryAttributeConverter {

    PmsCategoryAttributeForm toForm(PmsCategoryAttribute entity);

    PmsCategoryAttribute toEntity(PmsCategoryAttributeForm formData);
}
