package com.aioveu.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.oms.model.entity.OmsOrderItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: TODO 订单商品明细表
 * @Author: 雒世松
 * @Date: 2025/6/5 18:08
 * @param
 * @return:
 **/

@Mapper
public interface OrderItemMapper extends BaseMapper<OmsOrderItem> {

}
