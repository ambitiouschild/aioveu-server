package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.StoreImage;
import com.aioveu.vo.StoreImageDetailVO;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface StoreImageService extends IService<StoreImage> {


    /**
     * 删除图片
     * @param id
     * @return
     */
    boolean deleteImage(Long id);

    /**
     * 通过店铺id查询图片
     * @param storeId
     * @return
     */
    List<StoreImage> getByStoreId(Long storeId);

   /**
     * 管理平台活动详情
     * @param id
     * @return
     */
    StoreImageDetailVO managerDetail(Long id);

    /**
     * 获取列表
     * @param page
     * @param size
     * @param storeId
     * @return
     */
    IPage<StoreImageDetailVO> getManagerAll(int page, int size, Long storeId);




}
