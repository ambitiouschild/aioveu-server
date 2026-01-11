package com.aioveu.pms.aioveu04CategoryBrand.converter;

import com.aioveu.pms.aioveu04CategoryBrand.model.entity.PmsCategoryBrand;
import com.aioveu.pms.aioveu04CategoryBrand.model.form.PmsCategoryBrandForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: PmsCategoryBrandConverter
 * @Description TODO 商品分类与品牌关联表对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/11 20:07
 * @Version 1.0
 **/

@Mapper(componentModel = "spring")
public interface PmsCategoryBrandConverter {

    PmsCategoryBrandForm toForm(PmsCategoryBrand entity);

    PmsCategoryBrand toEntity(PmsCategoryBrandForm formData);
}
