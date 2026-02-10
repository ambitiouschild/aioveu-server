package com.aioveu.pay.aioveu02PayRefundRecord.service;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.pay.aioveu02PayRefundRecord.model.entity.PayRefundRecord;
import com.aioveu.pay.aioveu02PayRefundRecord.model.form.PayRefundRecordForm;
import com.aioveu.pay.aioveu02PayRefundRecord.model.query.PayRefundRecordQuery;
import com.aioveu.pay.aioveu02PayRefundRecord.model.query.RefundQueryDTO;
import com.aioveu.pay.aioveu02PayRefundRecord.model.vo.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: PayRefundRecordService
 * @Description TODO 退款记录服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 18:53
 * @Version 1.0
 **/
public interface PayRefundRecordService extends IService<PayRefundRecord> {

    /**
     *退款记录分页列表
     *
     * @return {@link IPage<PayRefundRecordVO>} 退款记录分页列表
     */
    IPage<PayRefundRecordVO> getPayRefundRecordPage(PayRefundRecordQuery queryParams);

    /**
     * 获取退款记录表单数据
     *
     * @param id 退款记录ID
     * @return 退款记录表单数据
     */
    PayRefundRecordForm getPayRefundRecordFormData(Long id);

    /**
     * 新增退款记录
     *
     * @param formData 退款记录表单对象
     * @return 是否新增成功
     */
    boolean savePayRefundRecord(PayRefundRecordForm formData);

    /**
     * 修改退款记录
     *
     * @param id   退款记录ID
     * @param formData 退款记录表单对象
     * @return 是否修改成功
     */
    boolean updatePayRefundRecord(Long id, PayRefundRecordForm formData);

    /**
     * 删除退款记录
     *
     * @param ids 退款记录ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deletePayRefundRecords(String ids);

    // 申请退款
    Result<String> applyRefund(PayRefundApplyDTO dto);

    // 处理退款回调
    Result<Void> handleRefundCallback(PayRefundCallbackDTO dto);

    // 查询退款状态
    Result<PayRefundStatusVO> queryRefundStatus(String refundNo);

    // 取消退款
    Result<Void> cancelRefund(String refundNo);

    // 分页查询退款记录
    Result<PageResult<PayRefundRecordVO>> queryRefundPage(RefundQueryDTO queryDTO);

    // 手动重试退款
    Result<Void> retryRefund(String refundNo);



}
