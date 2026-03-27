package com.aioveu.registry.aioveu07RegistryInvoiceInfo.converter;

import com.aioveu.registry.aioveu07RegistryInvoiceInfo.model.entity.RegistryInvoiceInfo;
import com.aioveu.registry.aioveu07RegistryInvoiceInfo.model.form.RegistryInvoiceInfoForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: RegistryInvoiceInfoConverter
 * @Description TODO 发票信息对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 18:48
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface RegistryInvoiceInfoConverter {

    RegistryInvoiceInfoForm toForm(RegistryInvoiceInfo entity);

    RegistryInvoiceInfo toEntity(RegistryInvoiceInfoForm formData);
}
