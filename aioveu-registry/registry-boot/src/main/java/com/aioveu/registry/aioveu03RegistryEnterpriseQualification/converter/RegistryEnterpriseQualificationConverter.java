package com.aioveu.registry.aioveu03RegistryEnterpriseQualification.converter;

import com.aioveu.registry.aioveu03RegistryEnterpriseQualification.model.entity.RegistryEnterpriseQualification;
import com.aioveu.registry.aioveu03RegistryEnterpriseQualification.model.form.RegistryEnterpriseQualificationForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: RegistryEnterpriseQualificationConverter
 * @Description TODO 企业资质对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 17:34
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface RegistryEnterpriseQualificationConverter {

    RegistryEnterpriseQualificationForm toForm(RegistryEnterpriseQualification entity);

    RegistryEnterpriseQualification toEntity(RegistryEnterpriseQualificationForm formData);
}
