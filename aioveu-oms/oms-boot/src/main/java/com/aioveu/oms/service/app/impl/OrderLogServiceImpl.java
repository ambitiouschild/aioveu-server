package com.aioveu.oms.service.app.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.common.security.util.SecurityUtils;
import com.aioveu.oms.mapper.OrderLogMapper;
import com.aioveu.oms.model.entity.OmsOrderLog;
import com.aioveu.oms.service.app.OrderLogService;
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
