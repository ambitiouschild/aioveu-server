package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.VenueSyncAlias;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface VenueSyncAliasService extends IService<VenueSyncAlias> {

    /**
     * 通过店铺id和平台编号查询对应别名
     * @param storeId
     * @param platformCode
     * @return
     */
    List<VenueSyncAlias> findByStoreIdAndPlatform(Long storeId, String platformCode);


}
