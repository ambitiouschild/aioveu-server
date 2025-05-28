package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.OrderDetail;
import com.aioveu.vo.BasicOrderVO;
import com.aioveu.vo.FieldOrderDetailVO;
import com.aioveu.vo.OrderDetailVO;
import com.aioveu.vo.UserEnterVO;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface OrderDetailService extends IService<OrderDetail> {

    /**
     * 根据消费码查找订单详情
     * @param code
     * @return
     */
    OrderDetail getByConsumeCode(String code);

    /**
     * 修改订单详情状态
     * @param orderDetailId
     * @param status
     * @return
     */
    boolean changeStatus(String orderDetailId, int status);


    /**
     * 获取未使用的订单详情数量
     * @param orderId
     * @return
     */
    Integer getUnUseOrderDetailCount(String orderId);

    /**
     * 获取报名人数
     * @param productId
     * @param categoryId
     * @param userId
     * @return
     */
    Integer getEnrollUserCount(String productId, Long categoryId, String userId);

    /**
     * 获取报名用户列表
     * @param page
     * @param size
     * @param productIds
     * @param categoryId
     * @return
     */
    IPage<UserEnterVO> getEnrollUserList(int page, int size, Collection<Long> productIds, Long categoryId);

    List<FieldOrderDetailVO> getFieldOrderDetailList(String orderId);

    /**
     * 获取订单详情
     * @param orderId
     * @param phone
     * @return
     */
    OrderDetailVO getOrderDetail(String orderId, String phone);

    /**
     * 通过订单id查找订单详情
     * @param orderId
     * @return
     */
    List<OrderDetail> getByOrderId(String orderId);

    /**
     * 通过订单id查找订单详情带用户名
     * @param orderId
     * @return
     */
    List<OrderDetailVO> getByOrderIdList(String orderId);

    /**
     * 通过订单id修改订单明细状态
     * @param orderId
     * @param status
     * @return
     */
    boolean changeStatusByOrderId(String orderId, int status);

    /**
     * 获取指定时间范围内的订场订单
     * @param storeId
     * @param start
     * @param end
     * @param status
     * @param payType
     * @return
     */
    IPage<BasicOrderVO> getFieldOrderDetailRangeAndStatus(int page, int size, Long storeId, Date start, Date end, String payType, Integer... status);

    /**
     * 同步订单明细新增的价格字段
     */
    void syncOrderDetail();
}
