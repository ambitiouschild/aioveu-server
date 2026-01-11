package com.aioveu.pms.aioveu01Brand.converter;

import com.aioveu.pms.aioveu01Brand.model.entity.PmsBrand;
import com.aioveu.pms.aioveu01Brand.model.form.PmsBrandForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: PmsBrandConverter
 * @Description TODO  商品品牌对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/10 19:04
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface PmsBrandConverter {

    PmsBrandForm toForm(PmsBrand entity);

    PmsBrand toEntity(PmsBrandForm formData);
}
