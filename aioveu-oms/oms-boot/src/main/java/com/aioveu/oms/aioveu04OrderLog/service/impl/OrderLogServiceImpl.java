package com.aioveu.oms.aioveu04OrderLog.service.impl;

import com.aioveu.common.security.util.SecurityUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.oms.aioveu04OrderLog.mapper.OrderLogMapper;
import com.aioveu.oms.aioveu04OrderLog.model.entity.OmsOrderLog;
import com.aioveu.oms.aioveu04OrderLog.service.OrderLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderLogServiceImpl extends ServiceImpl<OrderLogMapper, OmsOrderLog> implements OrderLogService {
    @Override
    public void addOrderLogs(Long orderId, Integer orderStatus, String user, String detail) {
        log.info("添加订单操作日志，orderId={}，detail={}", orderId, detail);
        OmsOrderLog orderLog = new OmsOrderLog();
        orderLog.setDetail(detail);
        orderLog.setOrderId(orderId);
        orderLog.setOrderStatus(orderStatus);
        orderLog.setUser(user);
        this.save(orderLog);
    }

    @Override
    public void addOrderLogs(Long orderId, Integer orderStatus, String detail) {
        Long memberId = SecurityUtils.getMemberId();
        addOrderLogs(orderId, orderStatus, memberId.toString(), detail);
    }

}
