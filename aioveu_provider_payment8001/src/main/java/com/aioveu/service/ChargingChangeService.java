package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.ChargingChange;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface ChargingChangeService extends IService<ChargingChange> {


    /**
     * 变动记录
     * @param chargingChange
     * @return
     */
    boolean change(ChargingChange chargingChange);


    /**
     * 获取增值服务变动明细
     * @param storeId
     * @param code
     * @param changeType
     * @param page
     * @param size
     * @return
     */
    IPage<ChargingChange> getChangeList(Long storeId, String code, Integer changeType, int page, int size);


}
