package com.aioveu.pay.aioveu06PayFlow.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.pay.aioveu06PayFlow.converter.PayFlowConverter;
import com.aioveu.pay.aioveu06PayFlow.mapper.PayFlowMapper;
import com.aioveu.pay.aioveu06PayFlow.model.entity.PayFlow;
import com.aioveu.pay.aioveu06PayFlow.model.form.PayFlowForm;
import com.aioveu.pay.aioveu06PayFlow.model.query.PayFlowQuery;
import com.aioveu.pay.aioveu06PayFlow.model.vo.PayFlowVO;
import com.aioveu.pay.aioveu06PayFlow.service.PayFlowService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: PayFlowServiceImpl
 * @Description TODO 支付流水服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/9 15:57
 * @Version 1.0
 **/

@Service
@RequiredArgsConstructor
public class PayFlowServiceImpl extends ServiceImpl<PayFlowMapper, PayFlow> implements PayFlowService {

    private final PayFlowConverter payFlowConverter;

    /**
     * 获取支付流水分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<PayFlowVO>} 支付流水分页列表
     */
    @Override
    public IPage<PayFlowVO> getPayFlowPage(PayFlowQuery queryParams) {
        Page<PayFlowVO> pageVO = this.baseMapper.getPayFlowPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取支付流水表单数据
     *
     * @param id 支付流水ID
     * @return 支付流水表单数据
     */
    @Override
    public PayFlowForm getPayFlowFormData(Long id) {
        PayFlow entity = this.getById(id);
        return payFlowConverter.toForm(entity);
    }

    /**
     * 新增支付流水
     *
     * @param formData 支付流水表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean savePayFlow(PayFlowForm formData) {
        PayFlow entity = payFlowConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新支付流水
     *
     * @param id   支付流水ID
     * @param formData 支付流水表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updatePayFlow(Long id,PayFlowForm formData) {
        PayFlow entity = payFlowConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除支付流水
     *
     * @param ids 支付流水ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deletePayFlows(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的支付流水数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }
}
