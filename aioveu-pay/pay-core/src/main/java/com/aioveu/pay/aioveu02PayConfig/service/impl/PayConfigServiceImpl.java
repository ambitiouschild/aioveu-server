package com.aioveu.pay.aioveu02PayConfig.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.pay.aioveu02PayConfig.converter.PayConfigConverter;
import com.aioveu.pay.aioveu02PayConfig.mapper.PayConfigMapper;
import com.aioveu.pay.aioveu02PayConfig.model.entity.PayConfig;
import com.aioveu.pay.aioveu02PayConfig.model.form.PayConfigForm;
import com.aioveu.pay.aioveu02PayConfig.model.query.PayConfigQuery;
import com.aioveu.pay.aioveu02PayConfig.model.vo.PayConfigVo;
import com.aioveu.pay.aioveu02PayConfig.service.PayConfigService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: PayConfigServiceImpl
 * @Description TODO 支付配置主表服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 16:08
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class PayConfigServiceImpl extends ServiceImpl<PayConfigMapper, PayConfig> implements PayConfigService {

    private final PayConfigConverter payConfigConverter;

    /**
     * 获取支付配置主表分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<PayConfigVo>} 支付配置主表分页列表
     */
    @Override
    public IPage<PayConfigVo> getPayConfigPage(PayConfigQuery queryParams) {
        Page<PayConfigVo> page = this.baseMapper.getPayConfigPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取支付配置主表表单数据
     *
     * @param id 支付配置主表ID
     * @return 支付配置主表表单数据
     */
    @Override
    public PayConfigForm getPayConfigFormData(Long id) {
        PayConfig entity = this.getById(id);
        return payConfigConverter.toForm(entity);
    }

    /**
     * 新增支付配置主表
     *
     * @param formData 支付配置主表表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean savePayConfig(PayConfigForm formData) {
        PayConfig entity = payConfigConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新支付配置主表
     *
     * @param id   支付配置主表ID
     * @param formData 支付配置主表表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updatePayConfig(Long id,PayConfigForm formData) {
        PayConfig entity = payConfigConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除支付配置主表
     *
     * @param ids 支付配置主表ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deletePayConfigs(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的支付配置主表数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }
}
