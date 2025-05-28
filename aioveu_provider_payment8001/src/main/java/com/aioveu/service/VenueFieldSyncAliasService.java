package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.VenueFieldSyncAlias;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface VenueFieldSyncAliasService extends IService<VenueFieldSyncAlias> {

    /**
     * 通过店铺id和平台编号查询对应场地别名
     * @param storeId
     * @param platformCode
     * @return
     */
    List<VenueFieldSyncAlias> findByStoreIdAndPlatform(Long storeId, String platformCode);


}
