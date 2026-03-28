package com.aioveu.pay.aioveu02PayConfig.service;

import com.aioveu.pay.aioveu02PayConfig.model.entity.PayConfig;
import com.aioveu.pay.aioveu02PayConfig.model.form.PayConfigForm;
import com.aioveu.pay.aioveu02PayConfig.model.query.PayConfigQuery;
import com.aioveu.pay.aioveu02PayConfig.model.vo.PayConfigVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: PayConfigService
 * @Description TODO 支付配置主表服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 16:07
 * @Version 1.0
 **/

public interface PayConfigService extends IService<PayConfig> {

    /**
     *支付配置主表分页列表
     *
     * @return {@link IPage<PayConfigVo>} 支付配置主表分页列表
     */
    IPage<PayConfigVo> getPayConfigPage(PayConfigQuery queryParams);

    /**
     * 获取支付配置主表表单数据
     *
     * @param id 支付配置主表ID
     * @return 支付配置主表表单数据
     */
    PayConfigForm getPayConfigFormData(Long id);

    /**
     * 新增支付配置主表
     *
     * @param formData 支付配置主表表单对象
     * @return 是否新增成功
     */
    boolean savePayConfig(PayConfigForm formData);

    /**
     * 修改支付配置主表
     *
     * @param id   支付配置主表ID
     * @param formData 支付配置主表表单对象
     * @return 是否修改成功
     */
    boolean updatePayConfig(Long id, PayConfigForm formData);

    /**
     * 删除支付配置主表
     *
     * @param ids 支付配置主表ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deletePayConfigs(String ids);
}
