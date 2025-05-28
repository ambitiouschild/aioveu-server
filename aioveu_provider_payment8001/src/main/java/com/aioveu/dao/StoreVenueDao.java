package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.StoreVenue;
import com.aioveu.vo.StoreVenueItemVO;
import org.springframework.stereotype.Repository;

/**
 * @description
 * @author: 雒世松
 * @date: 2023/01/17 10:42
 */
@Repository
public interface StoreVenueDao extends BaseMapper<StoreVenue> {


    /**
     * 获取店铺查找场馆
     *
     * @param page
     * @param storeId
     * @return
     */
    IPage<StoreVenueItemVO> getByStoreId(IPage<StoreVenueItemVO> page, Integer status, Long storeId);

}
