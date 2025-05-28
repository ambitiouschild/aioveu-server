package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.ChargingOption;
import com.aioveu.vo.IdNameCodeVO;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface ChargingOptionService extends IService<ChargingOption> {


    /**
     * 初始化店铺付费选项
     * @param storeId
     * @param companyId
     * @return
     */
    boolean initStoreChargingOption(Long storeId, Long companyId);


    /**
     * 获取按量列表
     * @return
     */
    List<IdNameCodeVO> getChargingOptionList();

    /**
     * 获取增值服务详情
     * @param code
     * @return
     */
    ChargingOption getByCode(String code);


}
