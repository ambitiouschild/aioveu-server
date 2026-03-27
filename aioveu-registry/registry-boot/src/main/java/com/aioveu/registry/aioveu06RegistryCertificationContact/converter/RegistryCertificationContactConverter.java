package com.aioveu.registry.aioveu06RegistryCertificationContact.converter;

import com.aioveu.registry.aioveu06RegistryCertificationContact.model.entity.RegistryCertificationContact;
import com.aioveu.registry.aioveu06RegistryCertificationContact.model.form.RegistryCertificationContactForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: RegistryCertificationContactConverter
 * @Description TODO 认证联系人对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 18:30
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface RegistryCertificationContactConverter {

    RegistryCertificationContactForm toForm(RegistryCertificationContact entity);

    RegistryCertificationContact toEntity(RegistryCertificationContactForm formData);
}
