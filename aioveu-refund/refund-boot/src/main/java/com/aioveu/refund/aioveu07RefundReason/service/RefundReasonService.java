package com.aioveu.refund.aioveu07RefundReason.service;

import com.aioveu.refund.aioveu07RefundReason.model.entity.RefundReason;
import com.aioveu.refund.aioveu07RefundReason.model.form.RefundReasonForm;
import com.aioveu.refund.aioveu07RefundReason.model.query.RefundReasonQuery;
import com.aioveu.refund.aioveu07RefundReason.model.vo.RefundReasonVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: RefundReasonService
 * @Description TODO 退款原因分类服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 13:56
 * @Version 1.0
 **/
public interface RefundReasonService extends IService<RefundReason> {

    /**
     *退款原因分类分页列表
     *
     * @return {@link IPage<RefundReasonVO>} 退款原因分类分页列表
     */
    IPage<RefundReasonVO> getRefundReasonPage(RefundReasonQuery queryParams);

    /**
     * 获取退款原因分类表单数据
     *
     * @param id 退款原因分类ID
     * @return 退款原因分类表单数据
     */
    RefundReasonForm getRefundReasonFormData(Long id);

    /**
     * 新增退款原因分类
     *
     * @param formData 退款原因分类表单对象
     * @return 是否新增成功
     */
    boolean saveRefundReason(RefundReasonForm formData);

    /**
     * 修改退款原因分类
     *
     * @param id   退款原因分类ID
     * @param formData 退款原因分类表单对象
     * @return 是否修改成功
     */
    boolean updateRefundReason(Long id, RefundReasonForm formData);

    /**
     * 删除退款原因分类
     *
     * @param ids 退款原因分类ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteRefundReasons(String ids);
}
