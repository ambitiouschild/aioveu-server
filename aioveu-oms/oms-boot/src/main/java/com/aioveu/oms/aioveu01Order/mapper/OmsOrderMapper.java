package com.aioveu.oms.aioveu01Order.mapper;

import com.aioveu.oms.aioveu01Order.model.entity.OmsOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aioveu.oms.aioveu01Order.model.vo.OrderBO;
import com.aioveu.oms.aioveu01Order.model.query.OrderPageQuery;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
}
