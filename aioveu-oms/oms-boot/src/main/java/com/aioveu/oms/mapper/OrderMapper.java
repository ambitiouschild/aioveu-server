package com.aioveu.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aioveu.oms.model.bo.OrderBO;
import com.aioveu.oms.model.entity.OmsOrder;
import com.aioveu.oms.model.query.OrderPageQuery;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: TODO 订单数据访问层
 * @Author: 雒世松
 * @Date: 2025/6/5 18:09
 * @param
 * @return:
 **/

@Mapper
public interface OrderMapper extends BaseMapper<OmsOrder> {

    /**
     * 订单分页列表
     *
     * @param page
     * @param queryParams
     * @return
     */
    Page<OrderBO> getOrderPage(Page<OrderBO> page, OrderPageQuery queryParams);
}
