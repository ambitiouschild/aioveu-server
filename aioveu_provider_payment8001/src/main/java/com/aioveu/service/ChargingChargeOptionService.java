package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.ChargingChargeOption;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface ChargingChargeOptionService extends IService<ChargingChargeOption> {

    /**
     * 获取按量的充值选项
     * @param chargingCode
     * @return
     */
    List<ChargingChargeOption> getChargeOptionByCode(String chargingCode);


}
