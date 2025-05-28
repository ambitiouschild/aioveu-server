package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.Order;
import com.aioveu.vo.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface OrderDao extends BaseMapper<Order> {

    /**
     * 获取店铺的订单
     * @param page
     * @param param
     * @return
     */
    IPage<OrderManagerVO> getStoreOrderList(IPage<OrderManagerVO> page, Map<String, Object> param);


    /**
     * 查询体验课订单
     * @param page
     * @param start
     * @param end
     * @param storeId
     * @param categoryId
     * @return
     */
    IPage<UserInfoOrderVO> getExperiential(IPage<UserInfoOrderVO> page, Date start, Date end, Long storeId, Long categoryId);


    /**
     * 查询主题的用户订单
     * @param page
     * @param userId
     * @param categoryId
     * @param status
     * @return
     */
    IPage<OrderTopicVO> getTopicList(IPage<OrderTopicVO> page, String userId, Long categoryId, Integer status);

    /**
     * 到店计划
     * @param userId
     * @param categoryId
     * @return
     */
    List<PushExerciseStoreVO> plan2Store(String userId, Long categoryId);

    /**
     * 获取分析订单列表
     * @param start
     * @param end
     * @param storeId
     * @param status
     * @param saleUserId
     * @return
     */
    List<AnalysisOrderVO> getAnalysisOrderList(Date start, Date end, Long storeId, Integer status, String saleUserId);

    List<AnalysisOrderVO> getOrderListById(List<String> orderIdList);

    IPage<FieldOrderManagerVO> getStoreFieldOrderList(IPage<OrderManagerVO> page, Map<String, Object> param);

    List<AnalysisOrderVO> getRenewalOrderList(Date start, Date end, Long storeId, List<Integer> statusList, String saleUserId);

    List<InvoiceRequestOrderVO> getInvoiceRequestOrderList(String start, String end, Long companyId, List<Integer> statusList, String userId);

    /**
     * 获取基本的订单信息列表 用于展示统计的订单明细
     * @param page
     * @param param
     * @return
     */
    IPage<BasicOrderVO> getBasicOrderList(IPage<BasicOrderVO> page, Map<String, Object> param);

}
