package com.aioveu.oms.service.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.oms.model.entity.OmsOrder;
import com.aioveu.oms.model.query.OrderPageQuery;
import com.aioveu.oms.model.vo.OmsOrderPageVO;

/**
 * @Description: TODO Admin-订单业务接口
 * @Author: 雒世松
 * @Date: 2025/6/5 18:13
 * @param
 * @return:
 **/

public interface OmsOrderService extends IService<OmsOrder> {
    /**
     * 订单分页列表
     *
     * @param queryParams {@link OrderPageQuery}
     * @return
     */
    IPage<OmsOrderPageVO> getOrderPage(OrderPageQuery queryParams);


}

