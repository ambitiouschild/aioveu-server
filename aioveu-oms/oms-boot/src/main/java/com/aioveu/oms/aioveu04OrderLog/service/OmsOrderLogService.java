package com.aioveu.oms.aioveu04OrderLog.service;

import com.aioveu.oms.aioveu04OrderLog.model.form.OmsOrderLogForm;
import com.aioveu.oms.aioveu04OrderLog.model.query.OmsOrderLogQuery;
import com.aioveu.oms.aioveu04OrderLog.model.vo.OmsOrderLogVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.oms.aioveu04OrderLog.model.entity.OmsOrderLog;

/**
 * @Description: TODO 订单操作历史记录服务类
 * @Author: 雒世松
 * @Date: 2025/6/5 18:15
 * @param
 * @return:
 **/

public interface OmsOrderLogService extends IService<OmsOrderLog> {


    /**
     *订单操作历史记录分页列表
     *
     * @return {@link IPage<OmsOrderLogVO>} 订单操作历史记录分页列表
     */
    IPage<OmsOrderLogVO> getOmsOrderLogPage(OmsOrderLogQuery queryParams);

    /**
     * 获取订单操作历史记录表单数据
     *
     * @param id 订单操作历史记录ID
     * @return 订单操作历史记录表单数据
     */
    OmsOrderLogForm getOmsOrderLogFormData(Long id);

    /**
     * 新增订单操作历史记录
     *
     * @param formData 订单操作历史记录表单对象
     * @return 是否新增成功
     */
    boolean saveOmsOrderLog(OmsOrderLogForm formData);

    /**
     * 修改订单操作历史记录
     *
     * @param id   订单操作历史记录ID
     * @param formData 订单操作历史记录表单对象
     * @return 是否修改成功
     */
    boolean updateOmsOrderLog(Long id, OmsOrderLogForm formData);

    /**
     * 删除订单操作历史记录
     *
     * @param ids 订单操作历史记录ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteOmsOrderLogs(String ids);



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

