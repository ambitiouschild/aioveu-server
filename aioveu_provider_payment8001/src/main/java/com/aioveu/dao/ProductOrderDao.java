package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.ProductOrder;
import com.aioveu.vo.ProductOrderManagerVO;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOrderDao extends BaseMapper<ProductOrder> {

    /**
     * 获取签单列表
     * @param page
     * @param id
     * @param name
     * @param categoryId
     * @param status
     * @param userId
     * @return
     */
    IPage<ProductOrderManagerVO> getManagerAll(IPage<ProductOrderManagerVO> page, String id, String name,
                                               Long categoryId, Integer status, String userId);
}
