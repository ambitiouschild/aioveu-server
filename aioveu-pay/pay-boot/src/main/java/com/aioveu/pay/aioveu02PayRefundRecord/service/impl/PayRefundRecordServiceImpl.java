package com.aioveu.pay.aioveu02PayRefundRecord.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.pay.aioveu02PayRefundRecord.converter.PayRefundRecordConverter;
import com.aioveu.pay.aioveu02PayRefundRecord.mapper.PayRefundRecordMapper;
import com.aioveu.pay.aioveu02PayRefundRecord.model.entity.PayRefundRecord;
import com.aioveu.pay.aioveu02PayRefundRecord.model.form.PayRefundRecordForm;
import com.aioveu.pay.aioveu02PayRefundRecord.model.query.PayRefundRecordQuery;
import com.aioveu.pay.aioveu02PayRefundRecord.model.vo.PayRefundRecordVO;
import com.aioveu.pay.aioveu02PayRefundRecord.service.PayRefundRecordService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: PayRefundRecordServiceImpl
 * @Description TODO 退款记录服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 18:54
 * @Version 1.0
 **/

@Service
@RequiredArgsConstructor
public class PayRefundRecordServiceImpl extends ServiceImpl<PayRefundRecordMapper, PayRefundRecord> implements PayRefundRecordService {

    private final PayRefundRecordConverter payRefundRecordConverter;

    /**
     * 获取退款记录分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<PayRefundRecordVO>} 退款记录分页列表
     */
    @Override
    public IPage<PayRefundRecordVO> getPayRefundRecordPage(PayRefundRecordQuery queryParams) {
        Page<PayRefundRecordVO> pageVO = this.baseMapper.getPayRefundRecordPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取退款记录表单数据
     *
     * @param id 退款记录ID
     * @return 退款记录表单数据
     */
    @Override
    public PayRefundRecordForm getPayRefundRecordFormData(Long id) {
        PayRefundRecord entity = this.getById(id);
        return payRefundRecordConverter.toForm(entity);
    }

    /**
     * 新增退款记录
     *
     * @param formData 退款记录表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean savePayRefundRecord(PayRefundRecordForm formData) {
        PayRefundRecord entity = payRefundRecordConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新退款记录
     *
     * @param id   退款记录ID
     * @param formData 退款记录表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updatePayRefundRecord(Long id,PayRefundRecordForm formData) {
        PayRefundRecord entity = payRefundRecordConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除退款记录
     *
     * @param ids 退款记录ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deletePayRefundRecords(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的退款记录数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }


}
