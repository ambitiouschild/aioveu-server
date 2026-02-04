package com.aioveu.pay.aioveu03PayChannelConfig.service;

import com.aioveu.pay.aioveu03PayChannelConfig.model.entity.PayChannelConfig;
import com.aioveu.pay.aioveu03PayChannelConfig.model.form.PayChannelConfigForm;
import com.aioveu.pay.aioveu03PayChannelConfig.model.query.PayChannelConfigQuery;
import com.aioveu.pay.aioveu03PayChannelConfig.model.vo.PayChannelConfigVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: PayChannelConfigService
 * @Description TODO 支付渠道配置服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 20:03
 * @Version 1.0
 **/
public interface PayChannelConfigService extends IService<PayChannelConfig> {

    /**
     *支付渠道配置分页列表
     *
     * @return {@link IPage<PayChannelConfigVO>} 支付渠道配置分页列表
     */
    IPage<PayChannelConfigVO> getPayChannelConfigPage(PayChannelConfigQuery queryParams);

    /**
     * 获取支付渠道配置表单数据
     *
     * @param id 支付渠道配置ID
     * @return 支付渠道配置表单数据
     */
    PayChannelConfigForm getPayChannelConfigFormData(Long id);

    /**
     * 新增支付渠道配置
     *
     * @param formData 支付渠道配置表单对象
     * @return 是否新增成功
     */
    boolean savePayChannelConfig(PayChannelConfigForm formData);

    /**
     * 修改支付渠道配置
     *
     * @param id   支付渠道配置ID
     * @param formData 支付渠道配置表单对象
     * @return 是否修改成功
     */
    boolean updatePayChannelConfig(Long id, PayChannelConfigForm formData);

    /**
     * 删除支付渠道配置
     *
     * @param ids 支付渠道配置ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deletePayChannelConfigs(String ids);
}
