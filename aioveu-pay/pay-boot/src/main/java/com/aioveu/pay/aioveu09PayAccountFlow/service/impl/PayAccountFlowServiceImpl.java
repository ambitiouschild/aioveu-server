package com.aioveu.pay.aioveu09PayAccountFlow.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.pay.aioveu09PayAccountFlow.converter.PayAccountFlowConverter;
import com.aioveu.pay.aioveu09PayAccountFlow.mapper.PayAccountFlowMapper;
import com.aioveu.pay.aioveu09PayAccountFlow.model.entity.PayAccountFlow;
import com.aioveu.pay.aioveu09PayAccountFlow.model.form.PayAccountFlowForm;
import com.aioveu.pay.aioveu09PayAccountFlow.model.query.PayAccountFlowQuery;
import com.aioveu.pay.aioveu09PayAccountFlow.model.vo.PayAccountFlowVO;
import com.aioveu.pay.aioveu09PayAccountFlow.service.PayAccountFlowService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: PayAccountFlowServiceImpl
 * @Description TODO 账户流水服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 16:31
 * @Version 1.0
 **/

@Service
@RequiredArgsConstructor
public class PayAccountFlowServiceImpl extends ServiceImpl<PayAccountFlowMapper, PayAccountFlow> implements PayAccountFlowService {

    private final PayAccountFlowConverter payAccountFlowConverter;

    /**
     * 获取账户流水分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<PayAccountFlowVO>} 账户流水分页列表
     */
    @Override
    public IPage<PayAccountFlowVO> getPayAccountFlowPage(PayAccountFlowQuery queryParams) {
        Page<PayAccountFlowVO> pageVO = this.baseMapper.getPayAccountFlowPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取账户流水表单数据
     *
     * @param id 账户流水ID
     * @return 账户流水表单数据
     */
    @Override
    public PayAccountFlowForm getPayAccountFlowFormData(Long id) {
        PayAccountFlow entity = this.getById(id);
        return payAccountFlowConverter.toForm(entity);
    }

    /**
     * 新增账户流水
     *
     * @param formData 账户流水表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean savePayAccountFlow(PayAccountFlowForm formData) {
        PayAccountFlow entity = payAccountFlowConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新账户流水
     *
     * @param id   账户流水ID
     * @param formData 账户流水表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updatePayAccountFlow(Long id,PayAccountFlowForm formData) {
        PayAccountFlow entity = payAccountFlowConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除账户流水
     *
     * @param ids 账户流水ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deletePayAccountFlows(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的账户流水数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

}
