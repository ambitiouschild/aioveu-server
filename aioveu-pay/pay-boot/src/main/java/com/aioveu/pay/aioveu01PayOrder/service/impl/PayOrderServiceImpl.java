package com.aioveu.pay.aioveu01PayOrder.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.pay.aioveu01PayOrder.converter.PayOrderConverter;
import com.aioveu.pay.aioveu01PayOrder.mapper.PayOrderMapper;
import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu01PayOrder.model.form.PayOrderForm;
import com.aioveu.pay.aioveu01PayOrder.model.query.PayOrderQuery;
import com.aioveu.pay.aioveu01PayOrder.model.vo.PayOrderVO;
import com.aioveu.pay.aioveu01PayOrder.service.PayOrderService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: PayOrderServiceImpl
 * @Description TODO 支付订单服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 17:33
 * @Version 1.0
 **/

@Service
@RequiredArgsConstructor
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder> implements PayOrderService {

    private final PayOrderConverter payOrderConverter;

    /**
     * 获取支付订单分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<PayOrderVO>} 支付订单分页列表
     */
    @Override
    public IPage<PayOrderVO> getPayOrderPage(PayOrderQuery queryParams) {
        Page<PayOrderVO> pageVO = this.baseMapper.getPayOrderPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取支付订单表单数据
     *
     * @param id 支付订单ID
     * @return 支付订单表单数据
     */
    @Override
    public PayOrderForm getPayOrderFormData(Long id) {
        PayOrder entity = this.getById(id);
        return payOrderConverter.toForm(entity);
    }

    /**
     * 新增支付订单
     *
     * @param formData 支付订单表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean savePayOrder(PayOrderForm formData) {
        PayOrder entity = payOrderConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新支付订单
     *
     * @param id   支付订单ID
     * @param formData 支付订单表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updatePayOrder(Long id,PayOrderForm formData) {
        PayOrder entity = payOrderConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除支付订单
     *
     * @param ids 支付订单ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deletePayOrders(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的支付订单数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }
}
