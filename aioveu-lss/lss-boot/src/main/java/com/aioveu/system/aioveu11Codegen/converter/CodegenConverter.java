package com.aioveu.system.aioveu11Codegen.converter;

import com.aioveu.system.aioveu11Codegen.model.entity.GenConfig;
import com.aioveu.system.aioveu11Codegen.model.entity.GenFieldConfig;
import com.aioveu.system.aioveu11Codegen.model.form.GenConfigForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @ClassName: CodegenConverter
 * @Description TODO 代码生成配置转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 17:31
 * @Version 1.0
 **/

@Mapper(componentModel = "spring")
public interface CodegenConverter {

    @Mapping(source = "genConfig.tableName", target = "tableName")
    @Mapping(source = "genConfig.businessName", target = "businessName")
    @Mapping(source = "genConfig.moduleName", target = "moduleName")
    @Mapping(source = "genConfig.packageName", target = "packageName")
    @Mapping(source = "genConfig.entityName", target = "entityName")
    @Mapping(source = "genConfig.author", target = "author")
    @Mapping(source = "fieldConfigs", target = "fieldConfigs")
    GenConfigForm toGenConfigForm(GenConfig genConfig, List<GenFieldConfig> fieldConfigs);

    List<GenConfigForm.FieldConfig> toGenFieldConfigForm(List<GenFieldConfig> fieldConfigs);

    GenConfigForm.FieldConfig toGenFieldConfigForm(GenFieldConfig genFieldConfig);

    GenConfig toGenConfig(GenConfigForm formData);

    List<GenFieldConfig> toGenFieldConfig(List<GenConfigForm.FieldConfig> fieldConfigs);

    GenFieldConfig toGenFieldConfig(GenConfigForm.FieldConfig fieldConfig);
}
