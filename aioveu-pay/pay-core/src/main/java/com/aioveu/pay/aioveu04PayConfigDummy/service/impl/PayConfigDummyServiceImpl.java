package com.aioveu.pay.aioveu04PayConfigDummy.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.pay.aioveu04PayConfigDummy.converter.PayConfigDummyConverter;
import com.aioveu.pay.aioveu04PayConfigDummy.mapper.PayConfigDummyMapper;
import com.aioveu.pay.aioveu04PayConfigDummy.model.entity.PayConfigDummy;
import com.aioveu.pay.aioveu04PayConfigDummy.model.form.PayConfigDummyForm;
import com.aioveu.pay.aioveu04PayConfigDummy.model.query.PayConfigDummyQuery;
import com.aioveu.pay.aioveu04PayConfigDummy.model.vo.PayConfigDummyVo;
import com.aioveu.pay.aioveu04PayConfigDummy.service.PayConfigDummyService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: PayConfigDummyServiceImpl
 * @Description TODO 模拟支付配置服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 16:32
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class PayConfigDummyServiceImpl extends ServiceImpl<PayConfigDummyMapper, PayConfigDummy> implements PayConfigDummyService {


    private final PayConfigDummyConverter payConfigDummyConverter;

    /**
     * 获取模拟支付配置分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<PayConfigDummyVo>} 模拟支付配置分页列表
     */
    @Override
    public IPage<PayConfigDummyVo> getPayConfigDummyPage(PayConfigDummyQuery queryParams) {
        Page<PayConfigDummyVo> page = this.baseMapper.getPayConfigDummyPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取模拟支付配置表单数据
     *
     * @param id 模拟支付配置ID
     * @return 模拟支付配置表单数据
     */
    @Override
    public PayConfigDummyForm getPayConfigDummyFormData(Long id) {
        PayConfigDummy entity = this.getById(id);
        return payConfigDummyConverter.toForm(entity);
    }

    /**
     * 新增模拟支付配置
     *
     * @param formData 模拟支付配置表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean savePayConfigDummy(PayConfigDummyForm formData) {
        PayConfigDummy entity = payConfigDummyConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新模拟支付配置
     *
     * @param id   模拟支付配置ID
     * @param formData 模拟支付配置表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updatePayConfigDummy(Long id,PayConfigDummyForm formData) {
        PayConfigDummy entity = payConfigDummyConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除模拟支付配置
     *
     * @param ids 模拟支付配置ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deletePayConfigDummys(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的模拟支付配置数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

}
