package com.aioveu.pay.aioveu05PayReconciliationDetail.service;

import com.aioveu.pay.aioveu05PayReconciliationDetail.model.entity.PayReconciliationDetail;
import com.aioveu.pay.aioveu05PayReconciliationDetail.model.form.PayReconciliationDetailForm;
import com.aioveu.pay.aioveu05PayReconciliationDetail.model.query.PayReconciliationDetailQuery;
import com.aioveu.pay.aioveu05PayReconciliationDetail.model.vo.PayReconciliationDetailVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: PayReconciliationDetailService
 * @Description TODO 对账明细服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/9 14:34
 * @Version 1.0
 **/

public interface PayReconciliationDetailService extends IService<PayReconciliationDetail> {

    /**
     *对账明细分页列表
     *
     * @return {@link IPage<PayReconciliationDetailVO>} 对账明细分页列表
     */
    IPage<PayReconciliationDetailVO> getPayReconciliationDetailPage(PayReconciliationDetailQuery queryParams);

    /**
     * 获取对账明细表单数据
     *
     * @param id 对账明细ID
     * @return 对账明细表单数据
     */
    PayReconciliationDetailForm getPayReconciliationDetailFormData(Long id);

    /**
     * 新增对账明细
     *
     * @param formData 对账明细表单对象
     * @return 是否新增成功
     */
    boolean savePayReconciliationDetail(PayReconciliationDetailForm formData);

    /**
     * 修改对账明细
     *
     * @param id   对账明细ID
     * @param formData 对账明细表单对象
     * @return 是否修改成功
     */
    boolean updatePayReconciliationDetail(Long id, PayReconciliationDetailForm formData);

    /**
     * 删除对账明细
     *
     * @param ids 对账明细ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deletePayReconciliationDetails(String ids);
}
