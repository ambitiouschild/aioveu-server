package com.aioveu.pay.aioveu13PayCallbackRecord.service;


import com.aioveu.pay.aioveu13PayCallbackRecord.model.entity.PayCallbackRecord;
import com.aioveu.pay.aioveu13PayCallbackRecord.model.form.PayCallbackRecordForm;
import com.aioveu.pay.aioveu13PayCallbackRecord.model.query.PayCallbackRecordQuery;
import com.aioveu.pay.aioveu13PayCallbackRecord.model.vo.PayCallbackRecordVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @ClassName: PayCallbackRecordService
 * @Description TODO 支付回调记录服务类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/29 18:12
 * @Version 1.0
 **/

public interface PayCallbackRecordService extends IService<PayCallbackRecord> {

    /**
     *支付回调记录分页列表
     *
     * @return {@link IPage<PayCallbackRecordVo>} 支付回调记录分页列表
     */
    IPage<PayCallbackRecordVo> getPayCallbackRecordPage(PayCallbackRecordQuery queryParams);

    /**
     * 获取支付回调记录表单数据
     *
     * @param id 支付回调记录ID
     * @return 支付回调记录表单数据
     */
    PayCallbackRecordForm getPayCallbackRecordFormData(Long id);

    /**
     * 新增支付回调记录
     *
     * @param formData 支付回调记录表单对象
     * @return 是否新增成功
     */
    boolean savePayCallbackRecord(PayCallbackRecordForm formData);

    /**
     * 修改支付回调记录
     *
     * @param id   支付回调记录ID
     * @param formData 支付回调记录表单对象
     * @return 是否修改成功
     */
    boolean updatePayCallbackRecord(Long id, PayCallbackRecordForm formData);

    /**
     * 删除支付回调记录
     *
     * @param ids 支付回调记录ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deletePayCallbackRecords(String ids);

    /**
     * 是否已处理过该回调（幂等判断）
     */
    boolean isConsumed(String transactionId);

    /**
     * 记录回调（含幂等）
     */
    void markConsumed(String transactionId, String paymentNo, String orderNo, Map<String, String> params);

    /**
     * 记录失败回调（含幂等）
     */
    void markFailed(
            String transactionId,
            String paymentNo,
            String orderNo,
            Map<String, String> params,
            String errorMsg);


    /**
     * incrNotifyCount= 每次微信 / 支付宝再次回调时调用
     */
    void incrNotifyCount(String transactionId);


    /*
    * 根据transactionId查找支付回调记录
    * */
    PayCallbackRecord getByTransactionId(String transactionId);

}
