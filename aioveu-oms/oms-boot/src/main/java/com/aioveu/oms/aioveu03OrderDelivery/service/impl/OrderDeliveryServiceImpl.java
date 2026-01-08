package com.aioveu.oms.aioveu03OrderDelivery.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.oms.aioveu03OrderDelivery.mapper.OrderDeliveryMapper;
import com.aioveu.oms.aioveu03OrderDelivery.model.entity.OmsOrderDelivery;
import com.aioveu.oms.aioveu03OrderDelivery.service.OrderDeliveryService;
import org.springframework.stereotype.Service;

@Service("orderDeliveryService")
public class OrderDeliveryServiceImpl extends ServiceImpl<OrderDeliveryMapper, OmsOrderDelivery> implements OrderDeliveryService {

}
