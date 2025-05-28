package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.OrderDetail;
import com.aioveu.vo.FieldOrderDetailVO;
import com.aioveu.vo.OrderDetailVO;
import com.aioveu.vo.UserEnterVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface OrderDetailDao extends BaseMapper<OrderDetail> {

    /**
     * 获取报名用户列表
     * @param page
     * @param productIds
     * @param categoryId
     * @return
     */
    IPage<UserEnterVO> getEnrollUserList(IPage<UserEnterVO> page, @Param("productIds") Collection<Long> productIds, @Param("categoryId") Long categoryId);

    /**
     * 获取订单详情
     * @param orderId
     * @return
     */
    OrderDetailVO getOrderDetail(String orderId);

    /**
     * 获取订单详情带用户名
     * @param orderId
     * @return
     */
    List<OrderDetailVO> getOrderDetailList(String orderId);

    /**
     * 获取订场订单详细
     * @param orderId
     * @return
     */
    List<FieldOrderDetailVO> getFieldOrderDetailList(String orderId);

}
