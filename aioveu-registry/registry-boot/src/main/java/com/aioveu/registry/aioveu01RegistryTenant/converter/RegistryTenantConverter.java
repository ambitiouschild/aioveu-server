package com.aioveu.registry.aioveu01RegistryTenant.converter;

import com.aioveu.registry.aioveu01RegistryTenant.model.entity.RegistryTenant;
import com.aioveu.registry.aioveu01RegistryTenant.model.form.RegistryTenantForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: RegistryTenantConverter
 * @Description TODO 租户注册小程序基本信息对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 16:32
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface RegistryTenantConverter {

    RegistryTenantForm toForm(RegistryTenant entity);

    RegistryTenant toEntity(RegistryTenantForm formData);
}
