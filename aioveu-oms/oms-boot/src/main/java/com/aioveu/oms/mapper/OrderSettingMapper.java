package com.aioveu.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.oms.model.entity.OmsOrderSetting;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: TODO 订单配置信息
 * @Author: 雒世松
 * @Date: 2025/6/5 18:09
 * @param
 * @return:
 **/

@Mapper
public interface OrderSettingMapper extends BaseMapper<OmsOrderSetting> {

}
