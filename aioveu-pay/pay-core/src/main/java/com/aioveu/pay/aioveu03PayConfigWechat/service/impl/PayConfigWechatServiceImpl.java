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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
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

}
