package com.aioveu.oms.service.app.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.oms.mapper.OrderItemMapper;
import com.aioveu.oms.model.entity.OmsOrderItem;
import com.aioveu.oms.service.app.OrderItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OmsOrderItem> implements OrderItemService {


}
