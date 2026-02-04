package com.aioveu.pay.aioveu04PayReconciliation.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.pay.aioveu04PayReconciliation.converter.PayReconciliationConverter;
import com.aioveu.pay.aioveu04PayReconciliation.mapper.PayReconciliationMapper;
import com.aioveu.pay.aioveu04PayReconciliation.model.entity.PayReconciliation;
import com.aioveu.pay.aioveu04PayReconciliation.model.form.PayReconciliationForm;
import com.aioveu.pay.aioveu04PayReconciliation.model.query.PayReconciliationQuery;
import com.aioveu.pay.aioveu04PayReconciliation.model.vo.PayReconciliationVO;
import com.aioveu.pay.aioveu04PayReconciliation.service.PayReconciliationService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: PayReconciliationServiceImpl
 * @Description TODO 支付对账服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 20:41
 * @Version 1.0
 **/

@Service
@RequiredArgsConstructor
public class PayReconciliationServiceImpl extends ServiceImpl<PayReconciliationMapper, PayReconciliation> implements PayReconciliationService {


    private final PayReconciliationConverter payReconciliationConverter;

    /**
     * 获取支付对账分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<PayReconciliationVO>} 支付对账分页列表
     */
    @Override
    public IPage<PayReconciliationVO> getPayReconciliationPage(PayReconciliationQuery queryParams) {
        Page<PayReconciliationVO> pageVO = this.baseMapper.getPayReconciliationPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取支付对账表单数据
     *
     * @param id 支付对账ID
     * @return 支付对账表单数据
     */
    @Override
    public PayReconciliationForm getPayReconciliationFormData(Long id) {
        PayReconciliation entity = this.getById(id);
        return payReconciliationConverter.toForm(entity);
    }

    /**
     * 新增支付对账
     *
     * @param formData 支付对账表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean savePayReconciliation(PayReconciliationForm formData) {
        PayReconciliation entity = payReconciliationConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新支付对账
     *
     * @param id   支付对账ID
     * @param formData 支付对账表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updatePayReconciliation(Long id,PayReconciliationForm formData) {
        PayReconciliation entity = payReconciliationConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除支付对账
     *
     * @param ids 支付对账ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deletePayReconciliations(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的支付对账数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

}
