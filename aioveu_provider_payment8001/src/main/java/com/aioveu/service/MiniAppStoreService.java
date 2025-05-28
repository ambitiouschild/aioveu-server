package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.MiniAppStore;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface MiniAppStoreService extends IService<MiniAppStore> {

    /**
     * 获取默认店铺id
     * @param appId
     * @return
     */
    Long getDefaultStoreId(String appId);

}
