package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.MiniAppStoreDao;
import com.aioveu.entity.MiniAppStore;
import com.aioveu.service.MiniAppStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class MiniAppStoreServiceImpl extends ServiceImpl<MiniAppStoreDao, MiniAppStore> implements MiniAppStoreService {


    @Override
    public Long getDefaultStoreId(String appId) {
        QueryWrapper<MiniAppStore> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MiniAppStore::getAppId, appId).eq(MiniAppStore::getDefaultStore, true);
        MiniAppStore miniAppStore = getOne(queryWrapper);
        if (miniAppStore != null) {
            return miniAppStore.getStoreId();
        }
        return null;
    }
}
