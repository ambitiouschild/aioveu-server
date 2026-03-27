package com.aioveu.registry.aioveu02RegistryAppAccount.converter;

import com.aioveu.registry.aioveu02RegistryAppAccount.model.entity.RegistryAppAccount;
import com.aioveu.registry.aioveu02RegistryAppAccount.model.form.RegistryAppAccountForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: RegistryAppAccountConverter
 * @Description TODO 小程序账号对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 17:06
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface RegistryAppAccountConverter {

    RegistryAppAccountForm toForm(RegistryAppAccount entity);

    RegistryAppAccount toEntity(RegistryAppAccountForm formData);
}
