package com.aioveu.sms.aioveu04CouponSpu.converter;

import com.aioveu.sms.aioveu04CouponSpu.model.entity.SmsCouponSpu;
import com.aioveu.sms.aioveu04CouponSpu.model.form.SmsCouponSpuForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: SmsCouponSpuConverter
 * @Description TODO 优惠券适用的具体商品对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/12 12:17
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface SmsCouponSpuConverter {

    SmsCouponSpuForm toForm(SmsCouponSpu entity);

    SmsCouponSpu toEntity(SmsCouponSpuForm formData);
}
