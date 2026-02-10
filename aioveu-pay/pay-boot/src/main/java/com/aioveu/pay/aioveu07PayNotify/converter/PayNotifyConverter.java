package com.aioveu.pay.aioveu07PayNotify.converter;

import com.aioveu.pay.aioveu07PayNotify.model.entity.PayNotify;
import com.aioveu.pay.aioveu07PayNotify.model.form.PayNotifyForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: PayNotifyConverter
 * @Description TODO 支付通知对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 15:59
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface PayNotifyConverter {

    PayNotifyForm toForm(PayNotify entity);

    PayNotify toEntity(PayNotifyForm formData);
}
