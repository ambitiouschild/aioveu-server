package com.aioveu.oms.aioveu01Order.service.app.impl;


import com.aioveu.common.security.util.SecurityUtils;
import com.aioveu.oms.aioveu01Order.mapper.OmsOrderMapper;
import com.aioveu.oms.aioveu01Order.model.entity.OmsOrder;
import com.aioveu.oms.aioveu01Order.model.query.OrderExportQuery;
import com.aioveu.oms.aioveu01Order.service.app.OrderExportService;
import com.aioveu.oms.aioveu01Order.service.app.OrderService;
import com.aioveu.oms.aioveu12OrderExportTask.enums.OrdeExportTaskStatusEnum;
import com.aioveu.oms.aioveu12OrderExportTask.mapper.OmsOrderExportTaskMapper;
import com.aioveu.oms.aioveu12OrderExportTask.model.entity.OmsOrderExportTask;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @ClassName: OrderExportServiceImpl
 * @Description TODO  订单导出服务实现类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/12 17:16
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderExportServiceImpl  implements OrderExportService {


    private final OmsOrderExportTaskMapper omsOrderExportTaskMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createExportTask(OrderExportQuery query, String token, String clientId) {

        // 1️解析用户
//        Jwt jwt = jwtDecoder.decode(token.replace("Bearer ", ""));
//        Long operatorId = jwt.getClaim("userId", Long.class);
//        Long tenantId = jwt.getClaim("tenantId", Long.class);
        Long operatorId = SecurityUtils.getMemberId();
        Long tenantId = SecurityUtils.getTenantId();

        // 2️权限校验
//        checkExportPermission(operatorId, tenantId);

        // 3️创建任务
        OmsOrderExportTask task = new OmsOrderExportTask();
        BeanUtils.copyProperties(query, task);
        task.setTenantId(tenantId);
        task.setOperatorId(operatorId);
        task.setClientId(clientId);
        task.setStatus(OrdeExportTaskStatusEnum.PENDING);
        task.setCreateTime(LocalDateTime.now());

        omsOrderExportTaskMapper.insert(task);
        return task.getId();
    }
}
