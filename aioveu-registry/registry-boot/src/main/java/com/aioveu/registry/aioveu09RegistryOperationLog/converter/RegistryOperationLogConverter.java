package com.aioveu.registry.aioveu09RegistryOperationLog.converter;

import com.aioveu.registry.aioveu09RegistryOperationLog.model.entity.RegistryOperationLog;
import com.aioveu.registry.aioveu09RegistryOperationLog.model.form.RegistryOperationLogForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: RegistryOperationLogConverter
 * @Description TODO 操作日志对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 19:29
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface RegistryOperationLogConverter {

    RegistryOperationLogForm toForm(RegistryOperationLog entity);

    RegistryOperationLog toEntity(RegistryOperationLogForm formData);
}
