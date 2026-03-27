package com.aioveu.registry.aioveu08RegistryAppFilingRecord.converter;

import com.aioveu.registry.aioveu08RegistryAppFilingRecord.model.entity.RegistryAppFilingRecord;
import com.aioveu.registry.aioveu08RegistryAppFilingRecord.model.form.RegistryAppFilingRecordForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: RegistryAppFilingRecordConverter
 * @Description TODO 小程序备案记录对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 19:16
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface RegistryAppFilingRecordConverter {

    RegistryAppFilingRecordForm toForm(RegistryAppFilingRecord entity);

    RegistryAppFilingRecord toEntity(RegistryAppFilingRecordForm formData);
}
