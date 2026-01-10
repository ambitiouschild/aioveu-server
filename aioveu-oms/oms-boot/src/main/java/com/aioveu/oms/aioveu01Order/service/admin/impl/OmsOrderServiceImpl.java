package com.aioveu.oms.aioveu01Order.service.admin.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.oms.aioveu01Order.model.entity.OmsOrder;
import com.aioveu.oms.aioveu01Order.model.form.OmsOrderForm;
import com.aioveu.oms.aioveu01Order.service.admin.OmsOrderService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.oms.aioveu01Order.converter.OmsOrderConverter;
import com.aioveu.oms.aioveu01Order.mapper.OmsOrderMapper;
import com.aioveu.oms.aioveu01Order.model.vo.OrderBO;
import com.aioveu.oms.aioveu01Order.model.query.OrderPageQuery;
import com.aioveu.oms.aioveu01Order.model.vo.OmsOrderPageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @Description: TODO Admin-订单业务实现类
 * @Author: 雒世松
 * @Date: 2025/6/5 18:13
 * @param
 * @return:
 **/

@Service
@RequiredArgsConstructor
public class OmsOrderServiceImpl extends ServiceImpl<OmsOrderMapper, OmsOrder> implements OmsOrderService {

    private final OmsOrderConverter omsOrderConverter;


    /**
     * Admin-订单分页列表
     *
     * @param queryParams {@link OrderPageQuery}
     * @return {@link OmsOrderPageVO}
     */
    @Override
    public IPage<OmsOrderPageVO> getOmsOrderPage(OrderPageQuery queryParams) {
        Page<OrderBO> boPage = this.baseMapper.getOrderPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams);

        return omsOrderConverter.toVoPage(boPage);
    }

    /**
     * 获取订单详情表单数据
     *
     * @param id 订单详情ID
     * @return 订单详情表单数据
     */
    @Override
    public OmsOrderForm getOmsOrderFormData(Long id) {
        OmsOrder entity = this.getById(id);
        return omsOrderConverter.toForm(entity);
    }

    /**
     * 新增订单详情
     *
     * @param formData 订单详情表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveOmsOrder(OmsOrderForm formData) {
        OmsOrder entity = omsOrderConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新订单详情
     *
     * @param id   订单详情ID
     * @param formData 订单详情表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateOmsOrder(Long id,OmsOrderForm formData) {
        OmsOrder entity = omsOrderConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除订单详情
     *
     * @param ids 订单详情ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteOmsOrders(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的订单详情数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

}
