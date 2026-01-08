package com.aioveu.oms.aioveu01Order.service.impl;

import com.aioveu.oms.aioveu01Order.model.entity.OmsOrder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.oms.aioveu01Order.converter.OrderConverter;
import com.aioveu.oms.aioveu01Order.mapper.OrderMapper;
import com.aioveu.oms.aioveu01Order.model.vo.OrderBO;
import com.aioveu.oms.aioveu01Order.model.query.OrderPageQuery;
import com.aioveu.oms.aioveu01Order.model.vo.OmsOrderPageVO;
import com.aioveu.oms.aioveu01Order.service.OmsOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Description: TODO Admin-订单业务实现类
 * @Author: 雒世松
 * @Date: 2025/6/5 18:13
 * @param
 * @return:
 **/

@Service
@RequiredArgsConstructor
public class OmsOrderServiceImpl extends ServiceImpl<OrderMapper, OmsOrder> implements OmsOrderService {

    private final OrderConverter orderConverter;


    /**
     * Admin-订单分页列表
     *
     * @param queryParams {@link OrderPageQuery}
     * @return {@link OmsOrderPageVO}
     */
    @Override
    public IPage<OmsOrderPageVO> getOrderPage(OrderPageQuery queryParams) {
        Page<OrderBO> boPage = this.baseMapper.getOrderPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams);

        return orderConverter.toVoPage(boPage);
    }

}
