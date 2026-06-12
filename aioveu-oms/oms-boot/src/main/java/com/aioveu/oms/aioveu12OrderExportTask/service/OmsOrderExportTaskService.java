package com.aioveu.oms.aioveu12OrderExportTask.service;


import com.aioveu.oms.aioveu01Order.model.query.OrderExportQuery;
import com.aioveu.oms.aioveu12OrderExportTask.model.entity.OmsOrderExportTask;
import com.aioveu.oms.aioveu12OrderExportTask.model.form.OmsOrderExportTaskForm;
import com.aioveu.oms.aioveu12OrderExportTask.model.query.OmsOrderExportTaskQuery;
import com.aioveu.oms.aioveu12OrderExportTask.model.vo.OmsOrderExportTaskVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * @ClassName: OmsOrderExportTaskService
 * @Description TODO 订单导出任务服务类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/12 18:25
 * @Version 1.0
 **/

public interface OmsOrderExportTaskService extends IService<OmsOrderExportTask> {

    /**
     *订单导出任务分页列表
     *
     * @return {@link IPage<OmsOrderExportTaskVo>} 订单导出任务分页列表
     */
    IPage<OmsOrderExportTaskVo> getOmsOrderExportTaskPage(OmsOrderExportTaskQuery queryParams);

    /**
     * 获取订单导出任务表单数据
     *
     * @param id 订单导出任务ID
     * @return 订单导出任务表单数据
     */
    OmsOrderExportTaskForm getOmsOrderExportTaskFormData(Long id);

    /**
     * 新增订单导出任务
     *
     * @param formData 订单导出任务表单对象
     * @return 是否新增成功
     */
    boolean saveOmsOrderExportTask(OmsOrderExportTaskForm formData);

    /**
     * 修改订单导出任务
     *
     * @param id   订单导出任务ID
     * @param formData 订单导出任务表单对象
     * @return 是否修改成功
     */
    boolean updateOmsOrderExportTask(Long id, OmsOrderExportTaskForm formData);

    /**
     * 删除订单导出任务
     *
     * @param ids 订单导出任务ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteOmsOrderExportTasks(String ids);


    /*
     * 创建订单导出任务
     * */
    Long createExportTask(OrderExportQuery query, String token, String clientId);


    /*
     * 下载订单
     * */
    boolean download(String exportNo, HttpServletResponse response) throws IOException;

}
