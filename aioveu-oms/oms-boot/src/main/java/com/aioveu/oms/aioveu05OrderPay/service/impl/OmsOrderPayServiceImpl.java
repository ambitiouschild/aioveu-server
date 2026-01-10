package com.aioveu.oms.aioveu05OrderPay.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.oms.aioveu05OrderPay.converter.OmsOrderPayConverter;
import com.aioveu.oms.aioveu05OrderPay.mapper.OmsOrderPayMapper;
import com.aioveu.oms.aioveu05OrderPay.model.entity.OmsOrderPay;
import com.aioveu.oms.aioveu05OrderPay.model.form.OmsOrderPayForm;
import com.aioveu.oms.aioveu05OrderPay.model.query.OmsOrderPayQuery;
import com.aioveu.oms.aioveu05OrderPay.model.vo.OmsOrderPayVO;
import com.aioveu.oms.aioveu05OrderPay.service.OmsOrderPayService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: OmsOrderPayServiceImpl
 * @Description TODO  支付信息服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/10 17:02
 * @Version 1.0
 **/

@Service
@RequiredArgsConstructor
public class OmsOrderPayServiceImpl extends ServiceImpl<OmsOrderPayMapper, OmsOrderPay> implements OmsOrderPayService {


    private final OmsOrderPayConverter omsOrderPayConverter;

    /**
     * 获取支付信息分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<OmsOrderPayVO>} 支付信息分页列表
     */
    @Override
    public IPage<OmsOrderPayVO> getOmsOrderPayPage(OmsOrderPayQuery queryParams) {
        Page<OmsOrderPayVO> pageVO = this.baseMapper.getOmsOrderPayPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取支付信息表单数据
     *
     * @param id 支付信息ID
     * @return 支付信息表单数据
     */
    @Override
    public OmsOrderPayForm getOmsOrderPayFormData(Long id) {
        OmsOrderPay entity = this.getById(id);
        return omsOrderPayConverter.toForm(entity);
    }

    /**
     * 新增支付信息
     *
     * @param formData 支付信息表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveOmsOrderPay(OmsOrderPayForm formData) {
        OmsOrderPay entity = omsOrderPayConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新支付信息
     *
     * @param id   支付信息ID
     * @param formData 支付信息表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateOmsOrderPay(Long id,OmsOrderPayForm formData) {
        OmsOrderPay entity = omsOrderPayConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除支付信息
     *
     * @param ids 支付信息ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteOmsOrderPays(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的支付信息数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }
}
