package com.aioveu.pay.aioveu05PayConfigAlipay.service;

import com.aioveu.pay.aioveu05PayConfigAlipay.model.entity.PayConfigAlipay;
import com.aioveu.pay.aioveu05PayConfigAlipay.model.form.PayConfigAlipayForm;
import com.aioveu.pay.aioveu05PayConfigAlipay.model.query.PayConfigAlipayQuery;
import com.aioveu.pay.aioveu05PayConfigAlipay.model.vo.PayConfigAlipayVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: PayConfigAlipayService
 * @Description TODO 支付宝支付配置服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 17:12
 * @Version 1.0
 **/

public interface PayConfigAlipayService extends IService<PayConfigAlipay> {

    /**
     *支付宝支付配置分页列表
     *
     * @return {@link IPage<PayConfigAlipayVo>} 支付宝支付配置分页列表
     */
    IPage<PayConfigAlipayVo> getPayConfigAlipayPage(PayConfigAlipayQuery queryParams);

    /**
     * 获取支付宝支付配置表单数据
     *
     * @param id 支付宝支付配置ID
     * @return 支付宝支付配置表单数据
     */
    PayConfigAlipayForm getPayConfigAlipayFormData(Long id);

    /**
     * 新增支付宝支付配置
     *
     * @param formData 支付宝支付配置表单对象
     * @return 是否新增成功
     */
    boolean savePayConfigAlipay(PayConfigAlipayForm formData);

    /**
     * 修改支付宝支付配置
     *
     * @param id   支付宝支付配置ID
     * @param formData 支付宝支付配置表单对象
     * @return 是否修改成功
     */
    boolean updatePayConfigAlipay(Long id, PayConfigAlipayForm formData);

    /**
     * 删除支付宝支付配置
     *
     * @param ids 支付宝支付配置ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deletePayConfigAlipays(String ids);
}
