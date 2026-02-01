package com.aioveu.refund.aioveu04RefundOperationLog.service;

import com.aioveu.refund.aioveu04RefundOperationLog.model.entity.RefundOperationLog;
import com.aioveu.refund.aioveu04RefundOperationLog.model.form.RefundOperationLogForm;
import com.aioveu.refund.aioveu04RefundOperationLog.model.query.RefundOperationLogQuery;
import com.aioveu.refund.aioveu04RefundOperationLog.model.vo.RefundOperationLogVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: RefundOperationLogService
 * @Description TODO 退款操作记录（用于审计）服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 18:20
 * @Version 1.0
 **/
public interface RefundOperationLogService extends IService<RefundOperationLog> {

    /**
     *退款操作记录（用于审计）分页列表
     *
     * @return {@link IPage<RefundOperationLogVO>} 退款操作记录（用于审计）分页列表
     */
    IPage<RefundOperationLogVO> getRefundOperationLogPage(RefundOperationLogQuery queryParams);

    /**
     * 获取退款操作记录（用于审计）表单数据
     *
     * @param id 退款操作记录（用于审计）ID
     * @return 退款操作记录（用于审计）表单数据
     */
    RefundOperationLogForm getRefundOperationLogFormData(Long id);

    /**
     * 新增退款操作记录（用于审计）
     *
     * @param formData 退款操作记录（用于审计）表单对象
     * @return 是否新增成功
     */
    boolean saveRefundOperationLog(RefundOperationLogForm formData);

    /**
     * 修改退款操作记录（用于审计）
     *
     * @param id   退款操作记录（用于审计）ID
     * @param formData 退款操作记录（用于审计）表单对象
     * @return 是否修改成功
     */
    boolean updateRefundOperationLog(Long id, RefundOperationLogForm formData);

    /**
     * 删除退款操作记录（用于审计）
     *
     * @param ids 退款操作记录（用于审计）ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteRefundOperationLogs(String ids);
}
