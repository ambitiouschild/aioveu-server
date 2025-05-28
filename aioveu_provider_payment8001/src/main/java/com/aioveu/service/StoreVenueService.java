package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.StoreVenue;
import com.aioveu.vo.FieldPlanTemplateVO;
import com.aioveu.vo.StoreVenueItemVO;
import com.aioveu.vo.StoreVenueVO;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2023/04/17 10:42
 */
public interface StoreVenueService extends IService<StoreVenue> {


    /**
     * 根据店铺获取场馆
     * @param page
     * @param size
     * @param storeId
     * @return
     */
    IPage<StoreVenueItemVO> getByStoreId(int page, int size, Integer status, Long storeId);

    List<StoreVenue> findByStoreId(Long storeId);

    StoreVenueVO getDetail(Long venueId);

    void saveStoreVenue(StoreVenueVO storeVenueVO);

    boolean changeStatus(Long id, Integer status);

    /**
     * 通过名称查找
     * @param name
     * @param storeId
     * @return
     */
    List<StoreVenue> getByName(String name, Long storeId);

}
