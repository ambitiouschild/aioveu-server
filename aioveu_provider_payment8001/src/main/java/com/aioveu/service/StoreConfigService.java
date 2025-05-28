package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.StoreConfig;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/02/19 10:42
 */
public interface StoreConfigService extends IService<StoreConfig> {

    /**
     * 获取店铺配置
     * @param dictCode
     * @param storeId
     * @return
     */
    StoreConfig getStoreConfig(String dictCode, long storeId);

    /**
     * 获取店铺配置列表
     * @param storeId
     * @param categoryCode
     * @return
     */
    List<StoreConfig> getStoreConfigList(Long storeId, String categoryCode);

    /**
     * 保存或者更新字典配置
     * @param storeConfig
     */
    StoreConfig saveOrUpdateStoreConfig(StoreConfig storeConfig);

    /**
     * 根据code获取所有门店的字典配置
     * @param dictCode
     * @return
     */
    List<StoreConfig> getAllStoreDictByCode(String dictCode);

    /**
     * 获取公司配置
     * @param companyId
     * @param dictCode
     * @return
     */
    StoreConfig getCompanyStoreConfig(Long companyId, String dictCode);

    /**
     * 获取公司所有配置
     * @param companyId
     * @return
     */
    List<StoreConfig> getAllCompanyConfig(Long companyId);

    /**
     * 更新公司配置
     * @param companyId
     * @param dictCode
     * @param value
     * @return
     */
    boolean updateCompanyStoreConfig(Long companyId, String dictCode, String value);

}
