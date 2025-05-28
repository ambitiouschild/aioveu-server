package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.entity.OrderRefund;
import com.aioveu.vo.BasicRefundOrderVO;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface OrderRefundDao extends BaseMapper<OrderRefund> {

    /**
     * 获取指定时间范围内的退款订单
     * @param storeId
     * @param start
     * @param end
     * @param categoryId
     * @return
     */
    List<BasicRefundOrderVO> getDateRangeRefundOrder(Long storeId, Date start, Date end, Long categoryId);

}
