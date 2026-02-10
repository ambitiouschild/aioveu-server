package com.aioveu.pay.aioveu08PayAccount.service;

import com.aioveu.pay.aioveu08PayAccount.model.entity.PayAccount;
import com.aioveu.pay.aioveu08PayAccount.model.form.PayAccountForm;
import com.aioveu.pay.aioveu08PayAccount.model.query.PayAccountQuery;
import com.aioveu.pay.aioveu08PayAccount.model.vo.PayAccountVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: PayAccountService
 * @Description TODO 支付账户服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 16:12
 * @Version 1.0
 **/

public interface PayAccountService extends IService<PayAccount> {

    /**
     *支付账户分页列表
     *
     * @return {@link IPage<PayAccountVO>} 支付账户分页列表
     */
    IPage<PayAccountVO> getPayAccountPage(PayAccountQuery queryParams);

    /**
     * 获取支付账户表单数据
     *
     * @param id 支付账户ID
     * @return 支付账户表单数据
     */
    PayAccountForm getPayAccountFormData(Long id);

    /**
     * 新增支付账户
     *
     * @param formData 支付账户表单对象
     * @return 是否新增成功
     */
    boolean savePayAccount(PayAccountForm formData);

    /**
     * 修改支付账户
     *
     * @param id   支付账户ID
     * @param formData 支付账户表单对象
     * @return 是否修改成功
     */
    boolean updatePayAccount(Long id, PayAccountForm formData);

    /**
     * 删除支付账户
     *
     * @param ids 支付账户ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deletePayAccounts(String ids);
}
