package com.aioveu.pay.aioveu07PayNotify.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.pay.aioveu07PayNotify.converter.PayNotifyConverter;
import com.aioveu.pay.aioveu07PayNotify.mapper.PayNotifyMapper;
import com.aioveu.pay.aioveu07PayNotify.model.entity.PayNotify;
import com.aioveu.pay.aioveu07PayNotify.model.form.PayNotifyForm;
import com.aioveu.pay.aioveu07PayNotify.model.query.PayNotifyQuery;
import com.aioveu.pay.aioveu07PayNotify.model.vo.PayNotifyVO;
import com.aioveu.pay.aioveu07PayNotify.service.PayNotifyService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: PayNotifyServiceImpl
 * @Description TODO 支付通知服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 16:00
 * @Version 1.0
 **/

@Service
@RequiredArgsConstructor
public class PayNotifyServiceImpl extends ServiceImpl<PayNotifyMapper, PayNotify> implements PayNotifyService {

    private final PayNotifyConverter payNotifyConverter;

    /**
     * 获取支付通知分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<PayNotifyVO>} 支付通知分页列表
     */
    @Override
    public IPage<PayNotifyVO> getPayNotifyPage(PayNotifyQuery queryParams) {
        Page<PayNotifyVO> pageVO = this.baseMapper.getPayNotifyPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取支付通知表单数据
     *
     * @param id 支付通知ID
     * @return 支付通知表单数据
     */
    @Override
    public PayNotifyForm getPayNotifyFormData(Long id) {
        PayNotify entity = this.getById(id);
        return payNotifyConverter.toForm(entity);
    }

    /**
     * 新增支付通知
     *
     * @param formData 支付通知表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean savePayNotify(PayNotifyForm formData) {
        PayNotify entity = payNotifyConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新支付通知
     *
     * @param id   支付通知ID
     * @param formData 支付通知表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updatePayNotify(Long id,PayNotifyForm formData) {
        PayNotify entity = payNotifyConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除支付通知
     *
     * @param ids 支付通知ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deletePayNotifys(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的支付通知数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }
}
