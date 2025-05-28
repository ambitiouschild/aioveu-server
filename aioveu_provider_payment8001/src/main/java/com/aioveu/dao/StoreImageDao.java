package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.StoreImage;
import com.aioveu.vo.StoreImageDetailVO;
import org.springframework.stereotype.Repository;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface StoreImageDao extends BaseMapper<StoreImage> {

    /**
     * 列表
     * @param page
     * @param storeId
     * @return
     */
    IPage<StoreImageDetailVO> getManagerAll(IPage<StoreImageDetailVO> page, Long storeId);



}
