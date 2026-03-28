package com.aioveu.pay.aioveu02PayConfig.converter;

import com.aioveu.pay.aioveu02PayConfig.model.entity.PayConfig;
import com.aioveu.pay.aioveu02PayConfig.model.form.PayConfigForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: PayConfigConverter
 * @Description TODO 支付配置主表对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 16:06
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface PayConfigConverter {

    PayConfigForm toForm(PayConfig entity);

    PayConfig toEntity(PayConfigForm formData);
}
