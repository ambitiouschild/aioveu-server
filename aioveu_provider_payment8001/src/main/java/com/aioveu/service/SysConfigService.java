package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.SysConfig;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/02/19 10:42
 */
public interface SysConfigService extends IService<SysConfig> {

    /**
     * 通过配置编号获取配置
     * @param code
     * @return
     */
    SysConfig getByCode(String code);

    /**
     * 获取分类配置列表
     * @param categoryCode
     * @return
     */
    List<SysConfig> getCategoryConfigList(String categoryCode);

}
