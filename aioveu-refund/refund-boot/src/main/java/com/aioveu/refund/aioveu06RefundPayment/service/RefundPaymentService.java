package com.aioveu.refund.aioveu06RefundPayment.service;

import com.aioveu.refund.aioveu06RefundPayment.model.entity.RefundPayment;
import com.aioveu.refund.aioveu06RefundPayment.model.form.RefundPaymentForm;
import com.aioveu.refund.aioveu06RefundPayment.model.query.RefundPaymentQuery;
import com.aioveu.refund.aioveu06RefundPayment.model.vo.RefundPaymentVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: RefundPaymentService
 * @Description TODO  退款支付记录服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 13:31
 * @Version 1.0
 **/
public interface RefundPaymentService extends IService<RefundPayment> {

    /**
     *退款支付记录分页列表
     *
     * @return {@link IPage<RefundPaymentVO>} 退款支付记录分页列表
     */
    IPage<RefundPaymentVO> getRefundPaymentPage(RefundPaymentQuery queryParams);

    /**
     * 获取退款支付记录表单数据
     *
     * @param id 退款支付记录ID
     * @return 退款支付记录表单数据
     */
    RefundPaymentForm getRefundPaymentFormData(Long id);

    /**
     * 新增退款支付记录
     *
     * @param formData 退款支付记录表单对象
     * @return 是否新增成功
     */
    boolean saveRefundPayment(RefundPaymentForm formData);

    /**
     * 修改退款支付记录
     *
     * @param id   退款支付记录ID
     * @param formData 退款支付记录表单对象
     * @return 是否修改成功
     */
    boolean updateRefundPayment(Long id, RefundPaymentForm formData);

    /**
     * 删除退款支付记录
     *
     * @param ids 退款支付记录ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteRefundPayments(String ids);
}
