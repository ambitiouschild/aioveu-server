package com.aioveu.oms.aioveu01Order.service.admin.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.oms.aioveu01Order.model.entity.OmsOrder;
import com.aioveu.oms.aioveu01Order.model.form.OmsOrderForm;
import com.aioveu.oms.aioveu01Order.model.vo.OrderDTO;
import com.aioveu.oms.aioveu01Order.service.admin.OmsOrderService;
import com.aioveu.oms.aioveu02OrderItem.model.entity.OmsOrderItem;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.oms.aioveu01Order.converter.OmsOrderConverter;
import com.aioveu.oms.aioveu01Order.mapper.OmsOrderMapper;
import com.aioveu.oms.aioveu01Order.model.vo.OrderBO;
import com.aioveu.oms.aioveu01Order.model.query.OrderPageQuery;
import com.aioveu.oms.aioveu01Order.model.vo.OmsOrderPageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @Description: TODO Admin-订单业务实现类
 * @Author: 雒世松
 * @Date: 2025/6/5 18:13
 * @param
 * @return:
 **/

@Slf4j
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
     * 根据订单编号查询订单详情
     *
     * @param orderNo {@link }
     * @return
     */
    @Override
    public OmsOrder getOrderDetailByOrderNo(String orderNo) {

        // 订单明细
        OmsOrder order = this.getOne(new LambdaQueryWrapper<OmsOrder>()
                .eq(OmsOrder::getOrderSn, orderNo)
        );
        return order;
    }

    /**
     * 根据微信返回结果更新订单状态
     *
     * @param orderNo
     * @param status
     * @return
     */
    @Override
    public boolean updateOrderStatusByWechatPay(String orderNo, Integer status) {

        log.info("更新订单状态, orderNo: {}, status: {}", orderNo, status);

        // 使用UpdateWrapper
        UpdateWrapper<OmsOrder> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("order_sn", orderNo)  // WHERE条件
                .set("status", status);

        // 执行更新
        boolean result = this.update(updateWrapper);
        log.info("更新结果: {}", result);

        return result;
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
