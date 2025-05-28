package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aioveu.entity.Store;
import com.aioveu.form.StoreForm;
import com.aioveu.vo.CategoryBaseVO;
import com.aioveu.vo.NearbyStoreVO;
import com.aioveu.vo.StoreMiniVO;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface StoreDao extends BaseMapper<Store> {

    /**
     * 小程序获取店铺列表
     * @param page
     * @param params
     * @return
     */
    IPage<StoreMiniVO> getMiniList(IPage<StoreMiniVO> page, @Param("params") Map<String, Object> params);

    /**
     * 查询店铺的产品分类
     * @param storeId
     * @return
     */
    List<CategoryBaseVO> getStoreCategory(Long storeId);

    /**
     * 根据距离和店铺查询是否在范围内
     * @param storeId
     * @param longitude
     * @param latitude
     * @return
     */
    Store getByIdLocation(Long storeId, Double longitude, Double latitude);

    /**
     * 查询附件10KM内的店铺
     * @param longitude
     * @param latitude
     * @return
     */
    List<NearbyStoreVO> getNearbyStore(Double longitude, Double latitude);

    /**
     * 获取用户列表
     * @param iPage
     * @param companyId
     * @param keyword
     * @return
     */
    IPage<StoreForm> getAll(Page<StoreForm> iPage, Long companyId, String keyword, Integer status);
}
