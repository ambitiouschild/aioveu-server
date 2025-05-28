package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.StoreProductCategoryDao;
import com.aioveu.entity.StoreProductCategory;
import com.aioveu.exception.SportException;
import com.aioveu.service.StoreProductCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description
 * @author: xiaoyao
 */
@Slf4j
@Service
public class StoreProductCategoryServiceImpl extends ServiceImpl<StoreProductCategoryDao, StoreProductCategory>
        implements StoreProductCategoryService {

    @Override
    public boolean batchCreateOrUpdate(Long storeId, String[] categoryIdArray) {
        if (categoryIdArray == null || categoryIdArray.length == 0) {
            throw new SportException("店铺分类id不能为空!");
        }
        LambdaQueryWrapper<StoreProductCategory> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StoreProductCategory::getStoreId, storeId);
        remove(wrapper);
        List<StoreProductCategory> storeProductCategories = new ArrayList<>(categoryIdArray.length);
        for (String categoryId : categoryIdArray) {
            StoreProductCategory item = new StoreProductCategory();
            item.setCategoryId(Long.parseLong(categoryId));
            item.setStoreId(storeId);
            storeProductCategories.add(item);
        }
        return saveBatch(storeProductCategories);
    }

    @Override
    public List<Long> getCategoryIdsByStoreId(Long storeId) {
        LambdaQueryWrapper<StoreProductCategory> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StoreProductCategory::getStoreId, storeId);
        return list(wrapper).stream().map(StoreProductCategory::getCategoryId).collect(Collectors.toList());
    }
}
