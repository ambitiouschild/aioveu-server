package com.aioveu.pay.aioveu03PayChannelConfig.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.pay.aioveu03PayChannelConfig.converter.PayChannelConfigConverter;
import com.aioveu.pay.aioveu03PayChannelConfig.mapper.PayChannelConfigMapper;
import com.aioveu.pay.aioveu03PayChannelConfig.model.entity.PayChannelConfig;
import com.aioveu.pay.aioveu03PayChannelConfig.model.form.PayChannelConfigForm;
import com.aioveu.pay.aioveu03PayChannelConfig.model.query.PayChannelConfigQuery;
import com.aioveu.pay.aioveu03PayChannelConfig.model.vo.PayChannelConfigVO;
import com.aioveu.pay.aioveu03PayChannelConfig.service.PayChannelConfigService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: PayChannelConfigServiceImpl
 * @Description TODO 支付渠道配置服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 20:04
 * @Version 1.0
 **/

@Service
@RequiredArgsConstructor
public class PayChannelConfigServiceImpl extends ServiceImpl<PayChannelConfigMapper, PayChannelConfig> implements PayChannelConfigService {

    private final PayChannelConfigConverter payChannelConfigConverter;

    /**
     * 获取支付渠道配置分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<PayChannelConfigVO>} 支付渠道配置分页列表
     */
    @Override
    public IPage<PayChannelConfigVO> getPayChannelConfigPage(PayChannelConfigQuery queryParams) {
        Page<PayChannelConfigVO> pageVO = this.baseMapper.getPayChannelConfigPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取支付渠道配置表单数据
     *
     * @param id 支付渠道配置ID
     * @return 支付渠道配置表单数据
     */
    @Override
    public PayChannelConfigForm getPayChannelConfigFormData(Long id) {
        PayChannelConfig entity = this.getById(id);
        return payChannelConfigConverter.toForm(entity);
    }

    /**
     * 新增支付渠道配置
     *
     * @param formData 支付渠道配置表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean savePayChannelConfig(PayChannelConfigForm formData) {
        PayChannelConfig entity = payChannelConfigConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新支付渠道配置
     *
     * @param id   支付渠道配置ID
     * @param formData 支付渠道配置表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updatePayChannelConfig(Long id,PayChannelConfigForm formData) {
        PayChannelConfig entity = payChannelConfigConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除支付渠道配置
     *
     * @param ids 支付渠道配置ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deletePayChannelConfigs(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的支付渠道配置数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }
}
