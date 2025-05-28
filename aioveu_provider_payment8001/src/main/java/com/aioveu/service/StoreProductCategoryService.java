package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.StoreProductCategory;

import java.util.List;

/**
 * @description
 * @author: xiaoyao
 */
public interface StoreProductCategoryService extends IService<StoreProductCategory> {

    /**
     * 批量创建
     * @param storeId
     * @param categoryIdArray
     * @return
     */
    boolean batchCreateOrUpdate(Long storeId, String[] categoryIdArray);

    /**
     * 查找店铺分类
     * @param storeId
     * @return
     */
    List<Long> getCategoryIdsByStoreId(Long storeId);

}
