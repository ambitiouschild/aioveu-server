package com.aioveu.pay.aioveu13PayCallbackRecord.service.impl;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.pay.aioveu13PayCallbackRecord.converter.PayCallbackRecordConverter;
import com.aioveu.pay.aioveu13PayCallbackRecord.mapper.PayCallbackRecordMapper;
import com.aioveu.pay.aioveu13PayCallbackRecord.model.entity.PayCallbackRecord;
import com.aioveu.pay.aioveu13PayCallbackRecord.model.form.PayCallbackRecordForm;
import com.aioveu.pay.aioveu13PayCallbackRecord.model.query.PayCallbackRecordQuery;
import com.aioveu.pay.aioveu13PayCallbackRecord.model.vo.PayCallbackRecordVo;
import com.aioveu.pay.aioveu13PayCallbackRecord.service.PayCallbackRecordService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: PayCallbackRecordServiceImpl
 * @Description TODO 支付回调记录服务实现类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/29 18:13
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class PayCallbackRecordServiceImpl extends ServiceImpl<PayCallbackRecordMapper, PayCallbackRecord> implements PayCallbackRecordService {

    private final PayCallbackRecordConverter payCallbackRecordConverter;

    /**
     * 获取支付回调记录分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<PayCallbackRecordVo>} 支付回调记录分页列表
     */
    @Override
    public IPage<PayCallbackRecordVo> getPayCallbackRecordPage(PayCallbackRecordQuery queryParams) {
        Page<PayCallbackRecordVo> page = this.baseMapper.getPayCallbackRecordPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取支付回调记录表单数据
     *
     * @param id 支付回调记录ID
     * @return 支付回调记录表单数据
     */
    @Override
    public PayCallbackRecordForm getPayCallbackRecordFormData(Long id) {
        PayCallbackRecord entity = this.getById(id);
        return payCallbackRecordConverter.toForm(entity);
    }

    /**
     * 新增支付回调记录
     *
     * @param formData 支付回调记录表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean savePayCallbackRecord(PayCallbackRecordForm formData) {
        PayCallbackRecord entity = payCallbackRecordConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新支付回调记录
     *
     * @param id   支付回调记录ID
     * @param formData 支付回调记录表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updatePayCallbackRecord(Long id,PayCallbackRecordForm formData) {
        PayCallbackRecord entity = payCallbackRecordConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除支付回调记录
     *
     * @param ids 支付回调记录ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deletePayCallbackRecords(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的支付回调记录数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }
}
