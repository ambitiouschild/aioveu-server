package com.aioveu.oms.aioveu04OrderLog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.oms.aioveu04OrderLog.model.entity.OmsOrderLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: TODO 订单操作历史记录
 * @Author: 雒世松
 * @Date: 2025/6/5 18:08
 * @param
 * @return:
 **/

@Mapper
public interface OrderLogMapper extends BaseMapper<OmsOrderLog> {

}
