package com.aioveu.pay.aioveu04PayReconciliation.service;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.pay.aioveu04PayReconciliation.model.entity.PayReconciliation;
import com.aioveu.pay.aioveu04PayReconciliation.model.form.PayReconciliationForm;
import com.aioveu.pay.aioveu04PayReconciliation.model.query.PayReconciliationQuery;
import com.aioveu.pay.aioveu04PayReconciliation.model.query.PayReconciliationQueryDTO;
import com.aioveu.pay.aioveu04PayReconciliation.model.vo.PayBillDownloadDTO;
import com.aioveu.pay.aioveu04PayReconciliation.model.vo.PayReconciliationHandleDifferenceDTO;
import com.aioveu.pay.aioveu04PayReconciliation.model.vo.PayReconciliationReportVO;
import com.aioveu.pay.aioveu04PayReconciliation.model.vo.PayReconciliationVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: PayReconciliationService
 * @Description TODO 支付对账服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 20:37
 * @Version 1.0
 **/
public interface PayReconciliationService extends IService<PayReconciliation> {

    /**
     *支付对账分页列表
     *
     * @return {@link IPage<PayReconciliationVO>} 支付对账分页列表
     */
    IPage<PayReconciliationVO> getPayReconciliationPage(PayReconciliationQuery queryParams);

    /**
     * 获取支付对账表单数据
     *
     * @param id 支付对账ID
     * @return 支付对账表单数据
     */
    PayReconciliationForm getPayReconciliationFormData(Long id);

    /**
     * 新增支付对账
     *
     * @param formData 支付对账表单对象
     * @return 是否新增成功
     */
    boolean savePayReconciliation(PayReconciliationForm formData);

    /**
     * 修改支付对账
     *
     * @param id   支付对账ID
     * @param formData 支付对账表单对象
     * @return 是否修改成功
     */
    boolean updatePayReconciliation(Long id, PayReconciliationForm formData);

    /**
     * 删除支付对账
     *
     * @param ids 支付对账ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deletePayReconciliations(String ids);


    // 下载对账单
    Result<Void> downloadBill(PayBillDownloadDTO dto);

    // 执行对账
    Result<Void> executeReconciliation(String reconciliationNo);

    // 生成对账报告
    Result<PayReconciliationReportVO> generateReport(String reconciliationNo);

    // 处理差异订单
    Result<Void> handleDifferenceOrder(PayReconciliationHandleDifferenceDTO dto);

    // 查询对账记录
    Result<PageResult<PayReconciliationVO>> queryReconciliationPage(PayReconciliationQueryDTO queryDTO);
}
