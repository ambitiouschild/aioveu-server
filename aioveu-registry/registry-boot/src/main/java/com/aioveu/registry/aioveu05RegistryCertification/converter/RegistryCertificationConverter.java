package com.aioveu.registry.aioveu05RegistryCertification.converter;

import com.aioveu.registry.aioveu05RegistryCertification.model.entity.RegistryCertification;
import com.aioveu.registry.aioveu05RegistryCertification.model.form.RegistryCertificationForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: RegistryCertificationConverter
 * @Description TODO 认证记录对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 18:18
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface RegistryCertificationConverter {

    RegistryCertificationForm toForm(RegistryCertification entity);

    RegistryCertification toEntity(RegistryCertificationForm formData);
}
