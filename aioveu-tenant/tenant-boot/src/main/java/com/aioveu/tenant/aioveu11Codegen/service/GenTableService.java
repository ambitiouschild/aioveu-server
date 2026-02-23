package com.aioveu.tenant.aioveu11Codegen.service;

import com.aioveu.tenant.aioveu11Codegen.model.entity.GenTable;
import com.aioveu.tenant.aioveu11Codegen.model.form.GenConfigForm;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: GenTableService
 * @Description TODO 代码生成配置接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 22:07
 * @Version 1.0
 **/
public interface GenTableService extends IService<GenTable> {

    /**
     * 获取代码生成配置
     *
     * @param tableName 表名
     * @return
     */
    GenConfigForm getGenTableFormData(String tableName);

    /**
     * 保存代码生成配置
     *
     * @param formData 表单数据
     * @return
     */
    void saveGenConfig(GenConfigForm formData);

    /**
     * 删除代码生成配置
     *
     * @param tableName 表名
     * @return
     */
    void deleteGenConfig(String tableName);
}
