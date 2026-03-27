package com.aioveu.registry.aioveu04RegistryAdministratorInfo.converter;

import com.aioveu.registry.aioveu04RegistryAdministratorInfo.model.entity.RegistryAdministratorInfo;
import com.aioveu.registry.aioveu04RegistryAdministratorInfo.model.form.RegistryAdministratorInfoForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: RegistryAdministratorInfoConverter
 * @Description TODO 管理员信息对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 17:46
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface RegistryAdministratorInfoConverter {

    RegistryAdministratorInfoForm toForm(RegistryAdministratorInfo entity);

    RegistryAdministratorInfo toEntity(RegistryAdministratorInfoForm formData);
}
