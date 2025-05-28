package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.StoreConfigDao;
import com.aioveu.entity.StoreConfig;
import com.aioveu.entity.SysConfig;
import com.aioveu.enums.DataStatus;
import com.aioveu.service.StoreConfigService;
import com.aioveu.service.SysConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/02/19 11:41
 */
@Service
public class StoreConfigServiceImpl extends ServiceImpl<StoreConfigDao, StoreConfig> implements StoreConfigService {

    @Autowired
    private SysConfigService sysConfigService;

    @Override
    public StoreConfig getStoreConfig(String dictCode, long storeId) {
        LambdaQueryWrapper<StoreConfig> eq = Wrappers.lambdaQuery(StoreConfig.class)
                .eq(StoreConfig::getCode, dictCode)
                .eq(StoreConfig::getCategoryCode, "store_config")
                .eq(StoreConfig::getStatus, DataStatus.NORMAL.getCode())
                .eq(StoreConfig::getStoreId, storeId);
        StoreConfig storeConfig = getOne(eq);
        if (storeConfig == null) {
            // 店铺未配置 获取默认配置
            SysConfig sysConfig = sysConfigService.getByCode(dictCode);
            if (sysConfig != null) {
                storeConfig = new StoreConfig();
                BeanUtils.copyProperties(sysConfig, storeConfig);
                storeConfig.setId(null);
                storeConfig.setStoreId(storeId);
                return storeConfig;
            }
            return null;
        } else {
            return storeConfig;
        }
    }

    @Override
    public StoreConfig getCompanyStoreConfig(Long companyId, String dictCode) {
        LambdaQueryWrapper<StoreConfig> eq = Wrappers.lambdaQuery(StoreConfig.class)
                .eq(StoreConfig::getCode, dictCode)
                .eq(StoreConfig::getCategoryCode, "company_config")
                .eq(StoreConfig::getStatus, DataStatus.NORMAL.getCode())
                .eq(StoreConfig::getCompanyId, companyId);
        StoreConfig storeConfig = getOne(eq);
        if (storeConfig == null) {
            // 店铺未配置 获取默认配置
            SysConfig sysConfig = sysConfigService.getByCode(dictCode);
            if (sysConfig != null) {
                storeConfig = new StoreConfig();
                BeanUtils.copyProperties(sysConfig, storeConfig);
                storeConfig.setId(null);
                storeConfig.setCompanyId(companyId);
                storeConfig.setCreateDate(null);
                storeConfig.setUpdateDate(null);
                return storeConfig;
            }
            return null;
        } else {
            return storeConfig;
        }
    }

    @Override
    public List<StoreConfig> getAllCompanyConfig(Long companyId) {
        String categoryCode = "company_config";
        List<SysConfig> categoryConfigList = sysConfigService.getCategoryConfigList(categoryCode);

        LambdaQueryWrapper<StoreConfig> eq = Wrappers.lambdaQuery(StoreConfig.class)
                .eq(StoreConfig::getCategoryCode, categoryCode)
                .eq(StoreConfig::getStatus, DataStatus.NORMAL.getCode())
                .eq(StoreConfig::getCompanyId, companyId);
        List<StoreConfig> storeConfigList = list(eq);
        Map<String, StoreConfig> storeConfigMap = storeConfigList.stream().collect(Collectors.toMap(
                StoreConfig::getCode,
                Function.identity()));
        List<StoreConfig> configList = new ArrayList<>();
        for (SysConfig sysConfig : categoryConfigList) {
            StoreConfig storeConfig;
            if (storeConfigMap.get(sysConfig.getCode()) != null) {
                storeConfig = storeConfigMap.get(sysConfig.getCode());
            } else {
                storeConfig = new StoreConfig();
                BeanUtils.copyProperties(sysConfig, storeConfig);
                storeConfig.setId(null);
                storeConfig.setCompanyId(companyId);
            }
            configList.add(storeConfig);
        }
        return configList;
    }

    @Override
    public List<StoreConfig> getStoreConfigList(Long storeId, String categoryCode) {
        List<SysConfig> categoryConfigList = sysConfigService.getCategoryConfigList(categoryCode);
        LambdaQueryWrapper<StoreConfig> eq = Wrappers.lambdaQuery(StoreConfig.class)
                .eq(StoreConfig::getStoreId, storeId)
                .eq(StoreConfig::getCategoryCode, categoryCode)
                .eq(StoreConfig::getStatus, DataStatus.NORMAL.getCode())
                .orderByDesc(StoreConfig::getCreateDate);
        List<StoreConfig> storeConfigList = list(eq);
        Map<String, StoreConfig> storeConfigMap = storeConfigList.stream().collect(Collectors.toMap(
                StoreConfig::getCode,
                Function.identity()));

        List<StoreConfig> configList = new ArrayList<>();
        for (SysConfig sysConfig : categoryConfigList) {
            StoreConfig storeConfig;
            if (storeConfigMap.get(sysConfig.getCode()) != null) {
                storeConfig = storeConfigMap.get(sysConfig.getCode());
            } else {
                storeConfig = new StoreConfig();
                BeanUtils.copyProperties(sysConfig, storeConfig);
                storeConfig.setId(null);
                storeConfig.setStoreId(storeId);
            }
            configList.add(storeConfig);
        }
        return configList;
    }

    @Override
    public StoreConfig saveOrUpdateStoreConfig(StoreConfig storeConfig) {
        if (storeConfig.getId() == null) {
            SysConfig sysConfig = sysConfigService.getByCode(storeConfig.getCode());
            StoreConfig sc = new StoreConfig();
            BeanUtils.copyProperties(sysConfig, sc);
            sc.setId(null);
            sc.setValue(storeConfig.getValue());
            sc.setStoreId(storeConfig.getStoreId());
            sc.setCompanyId(storeConfig.getCompanyId());
            sc.setRemark(storeConfig.getRemark());
            sc.setCreateDate(null);
            sc.setUpdateDate(null);
            save(sc);
            return sc;
        } else {
            updateById(storeConfig);
            return storeConfig;
        }
    }

    @Override
    public boolean updateCompanyStoreConfig(Long companyId, String dictCode, String value) {
        StoreConfig storeConfig = getCompanyStoreConfig(companyId, dictCode);
        storeConfig.setValue(value);
        return saveOrUpdate(storeConfig);
    }

    @Override
    public List<StoreConfig> getAllStoreDictByCode(String dictCode) {
        QueryWrapper<StoreConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StoreConfig::getCode, dictCode)
                .eq(StoreConfig::getStatus, DataStatus.NORMAL.getCode());
        return list(queryWrapper);
    }
}
