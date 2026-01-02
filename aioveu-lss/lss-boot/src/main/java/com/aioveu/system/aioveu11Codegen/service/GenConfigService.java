package com.aioveu.system.aioveu11Codegen.service;

import com.aioveu.system.aioveu11Codegen.model.entity.GenConfig;
import com.aioveu.system.aioveu11Codegen.model.form.GenConfigForm;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: GenConfigService
 * @Description TODO  代码生成配置接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 17:41
 * @Version 1.0
 **/
public interface GenConfigService extends IService<GenConfig> {

    /**
     * 获取代码生成配置
     *
     * @param tableName 表名
     * @return
     */
    GenConfigForm getGenConfigFormData(String tableName);

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
