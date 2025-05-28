package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.StoreChargingOption;
import com.aioveu.enums.ChargingOptionEnum;
import com.aioveu.vo.StoreChargingOptionDetailVO;
import com.aioveu.vo.StoreChargingOptionVO;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface StoreChargingOptionService extends IService<StoreChargingOption> {


    /**
     * 获取店铺按量付费选项
     * @param storeId
     * @return
     */
    List<StoreChargingOptionVO> getStoreChargingOption(Long storeId);

    /**
     * 根据店铺id和增值服务编号查询
     * @param storeId
     * @param code
     * @return
     */
    StoreChargingOption getByStoreIdAndCode(Long storeId, String code);

    /**
     * 通过公司ID查找增值服务
     * @param companyId
     * @param code
     * @return
     */
    StoreChargingOption getStoreChargingByCompanyId(Long companyId, String code);

    /**
     * 获取商户增值服务详情
     * @param chargingCode
     * @param storeId
     * @return
     */
    StoreChargingOptionDetailVO getStoreChargingOptionDetail(String chargingCode, Long storeId);

    /**
     * 增值服务扣费
     * @param companyId
     * @param storeId
     * @param chargingOption
     * @param count
     */
    void charging(Long companyId, Long storeId, ChargingOptionEnum chargingOption, int count);

    /**
     * 增值服务检查
     * @param companyId
     * @param storeId
     * @param chargingOption
     * @param count
     * @return
     */
    boolean chargingCheck(Long companyId, Long storeId, ChargingOptionEnum chargingOption, int count);

    /**
     * 增值服务余额不足提醒  没有剩余次数 提醒商户进行充值
     * @param companyId
     * @param storeId
     * @param chargingOption
     * @param tips
     */
    void noValueCharging(Long companyId, Long storeId, ChargingOptionEnum chargingOption, String tips);


}
