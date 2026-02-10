package com.aioveu.pay.aioveu09PayAccountFlow.service;

import com.aioveu.pay.aioveu09PayAccountFlow.model.entity.PayAccountFlow;
import com.aioveu.pay.aioveu09PayAccountFlow.model.form.PayAccountFlowForm;
import com.aioveu.pay.aioveu09PayAccountFlow.model.query.PayAccountFlowQuery;
import com.aioveu.pay.aioveu09PayAccountFlow.model.vo.PayAccountFlowVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: PayAccountFlowService
 * @Description TODO 账户流水服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 16:31
 * @Version 1.0
 **/

public interface PayAccountFlowService extends IService<PayAccountFlow> {

    /**
     *账户流水分页列表
     *
     * @return {@link IPage<PayAccountFlowVO>} 账户流水分页列表
     */
    IPage<PayAccountFlowVO> getPayAccountFlowPage(PayAccountFlowQuery queryParams);

    /**
     * 获取账户流水表单数据
     *
     * @param id 账户流水ID
     * @return 账户流水表单数据
     */
    PayAccountFlowForm getPayAccountFlowFormData(Long id);

    /**
     * 新增账户流水
     *
     * @param formData 账户流水表单对象
     * @return 是否新增成功
     */
    boolean savePayAccountFlow(PayAccountFlowForm formData);

    /**
     * 修改账户流水
     *
     * @param id   账户流水ID
     * @param formData 账户流水表单对象
     * @return 是否修改成功
     */
    boolean updatePayAccountFlow(Long id, PayAccountFlowForm formData);

    /**
     * 删除账户流水
     *
     * @param ids 账户流水ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deletePayAccountFlows(String ids);
}
