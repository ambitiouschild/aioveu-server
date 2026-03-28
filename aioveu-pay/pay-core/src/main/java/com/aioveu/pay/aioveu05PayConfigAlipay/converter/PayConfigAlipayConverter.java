package com.aioveu.pay.aioveu05PayConfigAlipay.converter;

import com.aioveu.pay.aioveu05PayConfigAlipay.model.entity.PayConfigAlipay;
import com.aioveu.pay.aioveu05PayConfigAlipay.model.form.PayConfigAlipayForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: PayConfigAlipayConverter
 * @Description TODO 支付宝支付配置对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 17:11
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface PayConfigAlipayConverter {

    PayConfigAlipayForm toForm(PayConfigAlipay entity);

    PayConfigAlipay toEntity(PayConfigAlipayForm formData);
}
