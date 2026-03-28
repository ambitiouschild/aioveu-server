package com.aioveu.pay.aioveu03PayConfigWechat.service;

import com.aioveu.pay.aioveu03PayConfigWechat.model.entity.PayConfigWechat;
import com.aioveu.pay.aioveu03PayConfigWechat.model.form.PayConfigWechatForm;
import com.aioveu.pay.aioveu03PayConfigWechat.model.query.PayConfigWechatQuery;
import com.aioveu.pay.aioveu03PayConfigWechat.model.vo.PayConfigWechatVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @ClassName: PayConfigWechatService
 * @Description TODO 微信支付配置服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 16:17
 * @Version 1.0
 **/

public interface PayConfigWechatService extends IService<PayConfigWechat> {

    /**
     *微信支付配置分页列表
     *
     * @return {@link IPage<PayConfigWechatVo>} 微信支付配置分页列表
     */
    IPage<PayConfigWechatVo> getPayConfigWechatPage(PayConfigWechatQuery queryParams);

    /**
     * 获取微信支付配置表单数据
     *
     * @param id 微信支付配置ID
     * @return 微信支付配置表单数据
     */
    PayConfigWechatForm getPayConfigWechatFormData(Long id);

    /**
     * 新增微信支付配置
     *
     * @param formData 微信支付配置表单对象
     * @return 是否新增成功
     */
    boolean savePayConfigWechat(PayConfigWechatForm formData);

    /**
     * 修改微信支付配置
     *
     * @param id   微信支付配置ID
     * @param formData 微信支付配置表单对象
     * @return 是否修改成功
     */
    boolean updatePayConfigWechat(Long id, PayConfigWechatForm formData);

    /**
     * 删除微信支付配置
     *
     * @param ids 微信支付配置ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deletePayConfigWechats(String ids);

    /**
     * 查询所有启用的微信支付配置
     */
    List<PayConfigWechat> listEnabledConfigs();

    /**
     * 根据租户ID和应用ID查询配置
     */
    PayConfigWechat getConfigByTenantAndApp(Long tenantId, String appId);

    /**
     * 获取默认配置
     */
    PayConfigWechat getDefaultConfig();
}
