package com.aioveu.system.aioveu08Config.service;

import com.aioveu.system.aioveu08Config.model.entity.Config;
import com.aioveu.system.aioveu08Config.model.form.ConfigForm;
import com.aioveu.system.aioveu08Config.model.query.ConfigPageQuery;
import com.aioveu.system.aioveu08Config.model.vo.ConfigVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
  *@ClassName: ConfigService
  *@Description TODO  系统配置Service接口
 *  *
  *@Author 可我不敌可爱
  *@Author 雒世松
  *@Date 2025/12/20 18:01
  *@Version 1.0
  **/
public interface ConfigService extends IService<Config> {

    /**
     * 分页查询系统配置
     * @param sysConfigPageQuery 查询参数
     * @return 系统配置分页列表
     */
    IPage<ConfigVO> page(ConfigPageQuery sysConfigPageQuery);

    /**
     * 保存系统配置
     * @param sysConfigForm 系统配置表单
     * @return 是否保存成功
     */
    boolean save(ConfigForm sysConfigForm);

    /**
     * 获取系统配置表单数据
     *
     * @param id 系统配置ID
     * @return 系统配置表单数据
     */
    ConfigForm getConfigFormData(Long id);

    /**
     * 编辑系统配置
     * @param id  系统配置ID
     * @param sysConfigForm 系统配置表单
     * @return 是否编辑成功
     */
    boolean edit(Long id, ConfigForm sysConfigForm);

    /**
     * 删除系统配置
     * @param ids 系统配置ID
     * @return 是否删除成功
     */
    boolean delete(Long ids);

    /**
     * 刷新系统配置缓存
     * @return 是否刷新成功
     */
    boolean refreshCache();

    /**
     * 获取系统配置
     * @param key 配置键
     * @return 配置值
     */
    Object getSystemConfig(String key);
}
