package com.aioveu.oms.aioveu01Order.mapper;

import com.aioveu.oms.aioveu01Order.model.entity.OmsOrder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aioveu.oms.aioveu01Order.model.vo.OrderBO;
import com.aioveu.oms.aioveu01Order.model.query.OrderPageQuery;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: TODO 订单数据访问层
 * @Author: 雒世松
 * @Date: 2025/6/5 18:09
 * @param
 * @return:
 **/
@Mapper
public interface OmsOrderMapper extends BaseMapper<OmsOrder> {

    /**
     * 订单分页列表  获取订单详情分页数据
     *
     * @param page
     * @param queryParams
     * @return
     */
    Page<OrderBO> getOrderPage(Page<OrderBO> page, OrderPageQuery queryParams);

    /**
     * 查询订单统计信息
     * @param queryParams 查询条件
     * @return 统计结果
     */
    @MapKey("status")
    Map<Integer, Map<String, Object>> getOrderStatusCounts(@Param("queryParams") OrderPageQuery queryParams);

    /**
     * 根据查询条件统计订单数量
     */
    Integer selectCountByQuery(@Param("queryParams") OrderPageQuery queryParams);

    /**
     * 根据状态列表统计订单数量
     */
    Integer selectCountByCondition(@Param("queryParams") OrderPageQuery queryParams,
                                   @Param("statusList") List<Integer> statusList);

    /**
     * 查询今日收入
     */
    Long selectTodayIncome(@Param("queryParams") OrderPageQuery queryParams);


    /**
     * 根据订单号查询订单
     */
//    @Select("SELECT * FROM oms_order WHERE order_no = #{orderNo} AND is_deleted = 0")
//    OmsOrder selectByOrderNo(@Param("orderNo") String orderNo);


    /**
     * 根据订单号查询订单
     */
    default OmsOrder selectByOrderNo(String orderNo) {
        LambdaQueryWrapper<OmsOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OmsOrder::getOrderSn, orderNo)
                .eq(OmsOrder::getDeleted, 0);
        return selectOne(wrapper);
    }



    /**
     * 根据租户ID和订单号查询
     */
//    @Select("SELECT * FROM oms_order WHERE tenant_id = #{tenantId} AND order_no = #{orderNo} AND is_deleted = 0")
//    OmsOrder selectByTenantIdAndOrderNo(@Param("tenantId") Long tenantId,
//                                        @Param("orderNo") String orderNo);

    /**
     * 根据租户ID和订单号查询
     */
    default OmsOrder selectByTenantIdAndOrderNo(Long tenantId, String orderNo) {
        LambdaQueryWrapper<OmsOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OmsOrder::getTenantId, tenantId)
                .eq(OmsOrder::getOrderSn, orderNo)
                .eq(OmsOrder::getDeleted, 0);
        return selectOne(wrapper);
    }


    /**
     * 更新订单状态
     */
//    @Update("""
//        UPDATE oms_order
//        SET status = #{status},
//            transaction_id = #{transactionId},
//            payment_time = #{paymentTime},
//            update_time = NOW()
//        WHERE id = #{id} AND is_deleted = 0
//    """)
//    int updateOrderStatus(@Param("id") Long id,
//                          @Param("status") Integer status,
//                          @Param("transactionId") String transactionId,
//                          @Param("paymentTime") Date paymentTime);


    /**
     * 根据订单号更新状态
     */
//    @Update("""
//        UPDATE oms_order
//        SET status = #{status},
//            pay_status = #{payStatus},
//            transaction_id = #{transactionId},
//            payment_time = #{paymentTime},
//            update_time = NOW()
//        WHERE order_no = #{orderNo}
//          AND status = #{oldStatus}  -- 乐观锁，确保状态从待支付更新
//          AND is_deleted = 0
//    """)
//    int updatePayStatusByOrderNo(@Param("orderNo") String orderNo,
//                                 @Param("oldStatus") Integer oldStatus,
//                                 @Param("status") Integer status,
//                                 @Param("payStatus") Integer payStatus,
//                                 @Param("transactionId") String transactionId,
//                                 @Param("paymentTime") Date paymentTime);


    /**
     * 查询订单是否存在
     */
//    @Select("SELECT COUNT(1) FROM oms_order WHERE order_no = #{orderNo} AND is_deleted = 0")
//    int existsByOrderNo(@Param("orderNo") String orderNo);


    /**
     * 根据用户ID分页查询订单
     */
    default Page<OmsOrder> selectPageByMemberId(Page<OmsOrder> page, Long memberId) {
        LambdaQueryWrapper<OmsOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OmsOrder::getMemberId, memberId)
                .eq(OmsOrder::getDeleted, 0)
                .orderByDesc(OmsOrder::getCreateTime);
        return selectPage(page, wrapper);
    }

    /**
     * 根据状态查询订单列表
     */
    default List<OmsOrder> selectByStatus(Integer status) {
        LambdaQueryWrapper<OmsOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OmsOrder::getStatus, status)
                .eq(OmsOrder::getDeleted, 0)
                .orderByDesc(OmsOrder::getCreateTime);
        return selectList(wrapper);
    }

    /**
     * 根据创建时间范围查询订单
     */
    default List<OmsOrder> selectByCreateTimeRange(Date startTime, Date endTime) {
        LambdaQueryWrapper<OmsOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(OmsOrder::getCreateTime, startTime)
                .le(OmsOrder::getCreateTime, endTime)
                .eq(OmsOrder::getDeleted, 0)
                .orderByDesc(OmsOrder::getCreateTime);
        return selectList(wrapper);
    }

    /**
     * 统计订单数量
     */
    default Long countOrders(Long tenantId, Integer status) {
        LambdaQueryWrapper<OmsOrder> wrapper = new LambdaQueryWrapper<>();

        if (tenantId != null) {
            wrapper.eq(OmsOrder::getTenantId, tenantId);
        }

        if (status != null) {
            wrapper.eq(OmsOrder::getStatus, status);
        }

        wrapper.eq(OmsOrder::getDeleted, 0);

        return Long.valueOf(selectCount(wrapper));
    }


    /**
     * 更新订单状态
     */
    default boolean updateOrderStatus(Long orderId, Integer oldStatus, Integer newStatus) {
        OmsOrder order = new OmsOrder();
        order.setId(orderId);
        order.setStatus(newStatus);
        order.setUpdateTime(LocalDateTime.now());

        LambdaQueryWrapper<OmsOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OmsOrder::getId, orderId)
                .eq(OmsOrder::getStatus, oldStatus)
                .eq(OmsOrder::getDeleted, 0);

        return update(order, wrapper) > 0;
    }

    /**
     * 批量更新订单状态
     */
    default int batchUpdateStatus(List<Long> orderIds, Integer status) {
        OmsOrder order = new OmsOrder();
        order.setStatus(status);
        order.setUpdateTime(LocalDateTime.now());

        LambdaQueryWrapper<OmsOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(OmsOrder::getId, orderIds)
                .eq(OmsOrder::getDeleted, 0);

        return update(order, wrapper);
    }


    /**
     * 自定义SQL查询：使用XML映射
     */
//    List<OmsOrder> selectOrdersByComplexConditions(@Param("params") Map<String, Object> params);

    /**
     * 注解方式查询
     */
    @Select("SELECT * FROM oms_order WHERE order_no = #{orderNo} AND is_deleted = 0")
    OmsOrder selectByOrderNoUsingAnnotation(@Param("orderNo") String orderNo);

    /**
     * 注解方式更新
     */
    @Update("""
        UPDATE oms_order 
        SET status = #{status}, 
            transaction_id = #{transactionId},
            payment_time = #{paymentTime},
            update_time = NOW()
        WHERE id = #{id} AND is_deleted = 0
    """)
    int updateOrderStatusUsingAnnotation(@Param("id") Long id,
                                         @Param("status") Integer status,
                                         @Param("transactionId") String transactionId,
                                         @Param("paymentTime") Date paymentTime);

}
