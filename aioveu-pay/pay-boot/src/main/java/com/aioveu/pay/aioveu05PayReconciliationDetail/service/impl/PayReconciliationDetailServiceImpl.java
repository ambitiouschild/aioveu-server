package com.aioveu.pay.aioveu05PayReconciliationDetail.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.pay.aioveu05PayReconciliationDetail.converter.PayReconciliationDetailConverter;
import com.aioveu.pay.aioveu05PayReconciliationDetail.mapper.PayReconciliationDetailMapper;
import com.aioveu.pay.aioveu05PayReconciliationDetail.model.entity.PayReconciliationDetail;
import com.aioveu.pay.aioveu05PayReconciliationDetail.model.form.PayReconciliationDetailForm;
import com.aioveu.pay.aioveu05PayReconciliationDetail.model.query.PayReconciliationDetailQuery;
import com.aioveu.pay.aioveu05PayReconciliationDetail.model.vo.PayReconciliationDetailVO;
import com.aioveu.pay.aioveu05PayReconciliationDetail.service.PayReconciliationDetailService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: PayReconciliationDetailServiceImpl
 * @Description TODO 对账明细服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/9 14:53
 * @Version 1.0
 **/

@Service
@RequiredArgsConstructor
public class PayReconciliationDetailServiceImpl extends ServiceImpl<PayReconciliationDetailMapper, PayReconciliationDetail> implements PayReconciliationDetailService {

    private final PayReconciliationDetailConverter payReconciliationDetailConverter;

    /**
     * 获取对账明细分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<PayReconciliationDetailVO>} 对账明细分页列表
     */
    @Override
    public IPage<PayReconciliationDetailVO> getPayReconciliationDetailPage(PayReconciliationDetailQuery queryParams) {
        Page<PayReconciliationDetailVO> pageVO = this.baseMapper.getPayReconciliationDetailPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取对账明细表单数据
     *
     * @param id 对账明细ID
     * @return 对账明细表单数据
     */
    @Override
    public PayReconciliationDetailForm getPayReconciliationDetailFormData(Long id) {
        PayReconciliationDetail entity = this.getById(id);
        return payReconciliationDetailConverter.toForm(entity);
    }

    /**
     * 新增对账明细
     *
     * @param formData 对账明细表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean savePayReconciliationDetail(PayReconciliationDetailForm formData) {
        PayReconciliationDetail entity = payReconciliationDetailConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新对账明细
     *
     * @param id   对账明细ID
     * @param formData 对账明细表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updatePayReconciliationDetail(Long id,PayReconciliationDetailForm formData) {
        PayReconciliationDetail entity = payReconciliationDetailConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除对账明细
     *
     * @param ids 对账明细ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deletePayReconciliationDetails(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的对账明细数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

}
