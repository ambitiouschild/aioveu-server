package com.aioveu.pay.aioveu05PayConfigAlipay.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.pay.aioveu05PayConfigAlipay.converter.PayConfigAlipayConverter;
import com.aioveu.pay.aioveu05PayConfigAlipay.mapper.PayConfigAlipayMapper;
import com.aioveu.pay.aioveu05PayConfigAlipay.model.entity.PayConfigAlipay;
import com.aioveu.pay.aioveu05PayConfigAlipay.model.form.PayConfigAlipayForm;
import com.aioveu.pay.aioveu05PayConfigAlipay.model.query.PayConfigAlipayQuery;
import com.aioveu.pay.aioveu05PayConfigAlipay.model.vo.PayConfigAlipayVo;
import com.aioveu.pay.aioveu05PayConfigAlipay.service.PayConfigAlipayService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: PayConfigAlipayServiceImpl
 * @Description TODO 支付宝支付配置服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 17:12
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class PayConfigAlipayServiceImpl extends ServiceImpl<PayConfigAlipayMapper, PayConfigAlipay> implements PayConfigAlipayService {



    private final PayConfigAlipayConverter payConfigAlipayConverter;

    /**
     * 获取支付宝支付配置分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<PayConfigAlipayVo>} 支付宝支付配置分页列表
     */
    @Override
    public IPage<PayConfigAlipayVo> getPayConfigAlipayPage(PayConfigAlipayQuery queryParams) {
        Page<PayConfigAlipayVo> page = this.baseMapper.getPayConfigAlipayPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取支付宝支付配置表单数据
     *
     * @param id 支付宝支付配置ID
     * @return 支付宝支付配置表单数据
     */
    @Override
    public PayConfigAlipayForm getPayConfigAlipayFormData(Long id) {
        PayConfigAlipay entity = this.getById(id);
        return payConfigAlipayConverter.toForm(entity);
    }

    /**
     * 新增支付宝支付配置
     *
     * @param formData 支付宝支付配置表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean savePayConfigAlipay(PayConfigAlipayForm formData) {
        PayConfigAlipay entity = payConfigAlipayConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新支付宝支付配置
     *
     * @param id   支付宝支付配置ID
     * @param formData 支付宝支付配置表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updatePayConfigAlipay(Long id,PayConfigAlipayForm formData) {
        PayConfigAlipay entity = payConfigAlipayConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除支付宝支付配置
     *
     * @param ids 支付宝支付配置ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deletePayConfigAlipays(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的支付宝支付配置数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

}
