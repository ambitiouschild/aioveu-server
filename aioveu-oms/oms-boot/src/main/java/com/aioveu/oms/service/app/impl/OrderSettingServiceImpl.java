package com.aioveu.oms.service.app.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.oms.mapper.OrderSettingMapper;
import com.aioveu.oms.model.entity.OmsOrderSetting;
import com.aioveu.oms.service.app.OrderSettingService;
import org.springframework.stereotype.Service;


@Service
public class OrderSettingServiceImpl extends ServiceImpl<OrderSettingMapper, OmsOrderSetting> implements OrderSettingService {

}
