package com.aioveu.pay.aioveu07PayNotify.service;

import com.aioveu.pay.aioveu07PayNotify.model.entity.PayNotify;
import com.aioveu.pay.aioveu07PayNotify.model.form.PayNotifyForm;
import com.aioveu.pay.aioveu07PayNotify.model.query.PayNotifyQuery;
import com.aioveu.pay.aioveu07PayNotify.model.vo.PayNotifyVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: PayNotifyService
 * @Description TODO 支付通知服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 15:59
 * @Version 1.0
 **/

public interface PayNotifyService extends IService<PayNotify> {

    /**
     *支付通知分页列表
     *
     * @return {@link IPage<PayNotifyVO>} 支付通知分页列表
     */
    IPage<PayNotifyVO> getPayNotifyPage(PayNotifyQuery queryParams);

    /**
     * 获取支付通知表单数据
     *
     * @param id 支付通知ID
     * @return 支付通知表单数据
     */
    PayNotifyForm getPayNotifyFormData(Long id);

    /**
     * 新增支付通知
     *
     * @param formData 支付通知表单对象
     * @return 是否新增成功
     */
    boolean savePayNotify(PayNotifyForm formData);

    /**
     * 修改支付通知
     *
     * @param id   支付通知ID
     * @param formData 支付通知表单对象
     * @return 是否修改成功
     */
    boolean updatePayNotify(Long id, PayNotifyForm formData);

    /**
     * 删除支付通知
     *
     * @param ids 支付通知ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deletePayNotifys(String ids);
}
