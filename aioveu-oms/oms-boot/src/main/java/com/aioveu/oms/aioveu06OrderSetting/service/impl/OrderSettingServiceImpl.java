package com.aioveu.oms.aioveu06OrderSetting.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.oms.aioveu06OrderSetting.mapper.OrderSettingMapper;
import com.aioveu.oms.aioveu06OrderSetting.model.entity.OmsOrderSetting;
import com.aioveu.oms.aioveu06OrderSetting.service.OrderSettingService;
import org.springframework.stereotype.Service;


@Service
public class OrderSettingServiceImpl extends ServiceImpl<OrderSettingMapper, OmsOrderSetting> implements OrderSettingService {

}
