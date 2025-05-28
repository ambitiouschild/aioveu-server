package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.Store;
import com.aioveu.entity.StoreProductCategory;
import com.aioveu.vo.StoreMiniVO;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @description
 * @author: xiaoyoa
 */
@Repository
public interface StoreProductCategoryDao extends BaseMapper<StoreProductCategory> {

}
