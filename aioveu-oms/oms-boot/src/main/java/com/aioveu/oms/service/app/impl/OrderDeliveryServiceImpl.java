package com.aioveu.oms.service.app.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.oms.mapper.OrderDeliveryMapper;
import com.aioveu.oms.model.entity.OmsOrderDelivery;
import com.aioveu.oms.service.app.OrderDeliveryService;
import org.springframework.stereotype.Service;

@Service("orderDeliveryService")
public class OrderDeliveryServiceImpl extends ServiceImpl<OrderDeliveryMapper, OmsOrderDelivery> implements OrderDeliveryService {

}
