package com.aioveu.oms.aioveu01Order.service.app;


import com.aioveu.oms.aioveu01Order.model.query.OrderExportQuery;

/**
 * @ClassName: OrderExportService
 * @Description TODO 订单导出服务类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/12 17:15
 * @Version 1.0
 **/

public interface OrderExportService {



    /*
    * 创建订单导出任务
    * */
    Long createExportTask(OrderExportQuery query, String token, String clientId);
}
