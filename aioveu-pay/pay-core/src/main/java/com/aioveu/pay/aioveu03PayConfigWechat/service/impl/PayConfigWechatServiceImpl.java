package com.aioveu.pay.aioveu03PayConfigWechat.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.pay.aioveu03PayConfigWechat.converter.PayConfigWechatConverter;
import com.aioveu.pay.aioveu03PayConfigWechat.mapper.PayConfigWechatMapper;
import com.aioveu.pay.aioveu03PayConfigWechat.model.entity.PayConfigWechat;
import com.aioveu.pay.aioveu03PayConfigWechat.model.form.PayConfigWechatForm;
import com.aioveu.pay.aioveu03PayConfigWechat.model.query.PayConfigWechatQuery;
import com.aioveu.pay.aioveu03PayConfigWechat.model.vo.PayConfigWechatVo;
import com.aioveu.pay.aioveu03PayConfigWechat.service.PayConfigWechatService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: PayConfigWechatServiceImpl
 * @Description TODO 微信支付配置服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 16:18
 * @Version 1.0
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class PayConfigWechatServiceImpl extends ServiceImpl<PayConfigWechatMapper, PayConfigWechat> implements PayConfigWechatService {


    private final PayConfigWechatConverter payConfigWechatConverter;

    /**
     * 获取微信支付配置分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<PayConfigWechatVo>} 微信支付配置分页列表
     */
    @Override
    public IPage<PayConfigWechatVo> getPayConfigWechatPage(PayConfigWechatQuery queryParams) {
        Page<PayConfigWechatVo> page = this.baseMapper.getPayConfigWechatPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取微信支付配置表单数据
     *
     * @param id 微信支付配置ID
     * @return 微信支付配置表单数据
     */
    @Override
    public PayConfigWechatForm getPayConfigWechatFormData(Long id) {
        PayConfigWechat entity = this.getById(id);
        return payConfigWechatConverter.toForm(entity);
    }

    /**
     * 新增微信支付配置
     *
     * @param formData 微信支付配置表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean savePayConfigWechat(PayConfigWechatForm formData) {
        PayConfigWechat entity = payConfigWechatConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新微信支付配置
     *
     * @param id   微信支付配置ID
     * @param formData 微信支付配置表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updatePayConfigWechat(Long id,PayConfigWechatForm formData) {
        PayConfigWechat entity = payConfigWechatConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除微信支付配置
     *
     * @param ids 微信支付配置ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deletePayConfigWechats(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的微信支付配置数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }


    @Override
    public List<PayConfigWechat> listEnabledConfigs() {
        LambdaQueryWrapper<PayConfigWechat> wrapper = new LambdaQueryWrapper<>();

        // 获取租户ID（根据你的业务逻辑）//自动过滤
        // 假设enabled=1表示启用 eq(PayConfigWechat::getEnabled, 1).
        wrapper .eq(PayConfigWechat::getIsDeleted, 0)
//                .eq(PayConfigWechat::getTenantId, tenantId)// 根据租户过滤
//                .orderByDesc(PayConfigWechat::getIsDefault)  // 默认配置在前
//                .orderByAsc(PayConfigWechat::getSort)  // 按排序
                .orderByDesc(PayConfigWechat::getCreateTime);  // 创建时间倒序

        List<PayConfigWechat> configs = this.list(wrapper);
        log.info("【PayConfigWechat】查询所有启用的微信支付配置：{}",configs);

        return configs;
    }

    @Override
    public PayConfigWechat getConfigByTenantAndApp(Long tenantId, String appId) {
        LambdaQueryWrapper<PayConfigWechat> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PayConfigWechat::getTenantId, tenantId)
                .eq(PayConfigWechat::getAppId, appId)
//                .eq(PayConfigWechat::getEnabled, 1)
                .eq(PayConfigWechat::getIsDeleted, 0)
                .last("LIMIT 1");

        return this.getOne(wrapper);
    }

    @Override
    public PayConfigWechat getDefaultConfig() {
        LambdaQueryWrapper<PayConfigWechat> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PayConfigWechat::getIsDeleted, 0)
//                .eq(PayConfigWechat::getEnabled, 1)
//                .eq(PayConfigWechat::getIsDefault, 1)  // 标记为默认
                .last("LIMIT 1");

        PayConfigWechat config = this.getOne(wrapper);

        // 如果没有默认配置，返回第一个启用的配置
        if (config == null) {
            wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(PayConfigWechat::getIsDeleted, 0)
//                    .eq(PayConfigWechat::getEnabled, 1)
                    .eq(PayConfigWechat::getIsDeleted, 0)
//                    .orderByAsc(PayConfigWechat::getSort)
                    .orderByDesc(PayConfigWechat::getCreateTime)
                    .last("LIMIT 1");

            config = this.getOne(wrapper);
        }

        return config;
    }



}
