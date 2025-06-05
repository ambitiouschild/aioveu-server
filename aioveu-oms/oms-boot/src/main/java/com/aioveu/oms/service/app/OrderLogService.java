package com.aioveu.oms.service.app;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.oms.model.entity.OmsOrderLog;

/**
 * @Description: TODO 订单操作历史记录
 * @Author: 雒世松
 * @Date: 2025/6/5 18:15
 * @param
 * @return:
 **/

public interface OrderLogService extends IService<OmsOrderLog> {

    /**
     * 添加订单操作日志记录
     * @param orderId 订单ID
     * @param orderStatus 订单状态
     * @param user 操作人员
     * @param detail 描述信息
     */
    void addOrderLogs(Long orderId, Integer orderStatus, String user, String detail);

    /**
     * 添加订单操作日志记录
     * @param orderId 订单ID
     * @param orderStatus 订单状态
     * @param detail 描述
     */
    void addOrderLogs(Long orderId, Integer orderStatus, String detail);
}

