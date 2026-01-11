package com.aioveu.pms.aioveu02Category.converter;

import com.aioveu.pms.aioveu02Category.model.entity.PmsCategory;
import com.aioveu.pms.aioveu02Category.model.form.PmsCategoryForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: PmsCategoryConverter
 * @Description TODO  商品分类对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/11 17:33
 * @Version 1.0
 **/

@Mapper(componentModel = "spring")
public interface PmsCategoryConverter {


    PmsCategoryForm toForm(PmsCategory entity);

    PmsCategory toEntity(PmsCategoryForm formData);
}
