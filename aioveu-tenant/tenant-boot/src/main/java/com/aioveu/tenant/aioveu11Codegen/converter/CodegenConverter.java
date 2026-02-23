package com.aioveu.tenant.aioveu11Codegen.converter;

import com.aioveu.tenant.aioveu11Codegen.model.entity.GenTable;
import com.aioveu.tenant.aioveu11Codegen.model.entity.GenTableColumn;
import com.aioveu.tenant.aioveu11Codegen.model.form.GenConfigForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @ClassName: CodegenConverter
 * @Description TODO 代码生成配置转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 22:04
 * @Version 1.0
 **/

@Mapper(componentModel = "spring")
public interface CodegenConverter {

    @Mapping(source = "genTable.tableName", target = "tableName")
    @Mapping(source = "genTable.businessName", target = "businessName")
    @Mapping(source = "genTable.moduleName", target = "moduleName")
    @Mapping(source = "genTable.packageName", target = "packageName")
    @Mapping(source = "genTable.entityName", target = "entityName")
    @Mapping(source = "genTable.author", target = "author")
    @Mapping(source = "genTable.pageType", target = "pageType")
    @Mapping(source = "genTable.removeTablePrefix", target = "removeTablePrefix")
    @Mapping(source = "fieldConfigs", target = "fieldConfigs")
    GenConfigForm toGenConfigForm(GenTable genTable, List<GenTableColumn> fieldConfigs);

    List<GenConfigForm.FieldConfig> toGenTableColumnForm(List<GenTableColumn> fieldConfigs);

    GenConfigForm.FieldConfig toGenTableColumnForm(GenTableColumn genTableColumn);

    GenTable toGenTable(GenConfigForm formData);

    List<GenTableColumn> toGenTableColumn(List<GenConfigForm.FieldConfig> fieldConfigs);

    GenTableColumn toGenTableColumn(GenConfigForm.FieldConfig fieldConfig);

}
