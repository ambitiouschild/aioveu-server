package com.aioveu.pay.aioveu06PayFlow.service;

import com.aioveu.pay.aioveu06PayFlow.model.entity.PayFlow;
import com.aioveu.pay.aioveu06PayFlow.model.form.PayFlowForm;
import com.aioveu.pay.aioveu06PayFlow.model.query.PayFlowQuery;
import com.aioveu.pay.aioveu06PayFlow.model.vo.PayFlowVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: PayFlowService
 * @Description TODO 支付流水服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/9 15:56
 * @Version 1.0
 **/

public interface PayFlowService extends IService<PayFlow> {


    /**
     *支付流水分页列表
     *
     * @return {@link IPage<PayFlowVO>} 支付流水分页列表
     */
    IPage<PayFlowVO> getPayFlowPage(PayFlowQuery queryParams);

    /**
     * 获取支付流水表单数据
     *
     * @param id 支付流水ID
     * @return 支付流水表单数据
     */
    PayFlowForm getPayFlowFormData(Long id);

    /**
     * 新增支付流水
     *
     * @param formData 支付流水表单对象
     * @return 是否新增成功
     */
    boolean savePayFlow(PayFlowForm formData);

    /**
     * 修改支付流水
     *
     * @param id   支付流水ID
     * @param formData 支付流水表单对象
     * @return 是否修改成功
     */
    boolean updatePayFlow(Long id, PayFlowForm formData);

    /**
     * 删除支付流水
     *
     * @param ids 支付流水ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deletePayFlows(String ids);
}
