package com.aioveu.sms.aioveu05CouponSpuCategory.converter;

import com.aioveu.sms.aioveu05CouponSpuCategory.model.entity.SmsCouponSpuCategory;
import com.aioveu.sms.aioveu05CouponSpuCategory.model.form.SmsCouponSpuCategoryForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: SmsCouponSpuCategoryConverter
 * @Description TODO 优惠券适用的具体分类对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/12 13:10
 * @Version 1.0
 **/

@Mapper(componentModel = "spring")
public interface SmsCouponSpuCategoryConverter {

    SmsCouponSpuCategoryForm toForm(SmsCouponSpuCategory entity);

    SmsCouponSpuCategory toEntity(SmsCouponSpuCategoryForm formData);
}
