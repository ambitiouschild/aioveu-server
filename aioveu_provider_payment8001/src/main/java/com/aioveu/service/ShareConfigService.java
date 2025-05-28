package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.dto.ShareConfigDTO;
import com.aioveu.entity.Order;
import com.aioveu.entity.ShareConfig;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface ShareConfigService extends IService<ShareConfig> {

    /**
     * 通过分类和产品id查找配置
     * @param companyId
     * @param categoryId
     * @param productId
     * @param storeId
     * @param shareType
     * @return
     */
    ShareConfig getByCategoryIdAndProductId(Long companyId, Long categoryId, String productId, Long storeId, Integer shareType);

    /**
     * 分享奖励
     * @param shareConfigDTO
     * @param order
     * @return
     */
    boolean shareReward(ShareConfigDTO shareConfigDTO, Order order);


}
