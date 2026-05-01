package com.aioveu.oms.aioveu02OrderItem.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.oms.aioveu01Order.mapper.OmsOrderMapper;
import com.aioveu.oms.aioveu01Order.model.entity.OmsOrder;
import com.aioveu.oms.aioveu02OrderItem.converter.OmsOrderItemConverter;
import com.aioveu.oms.aioveu02OrderItem.model.form.OmsOrderItemForm;
import com.aioveu.oms.aioveu02OrderItem.model.query.OmsOrderItemQuery;
import com.aioveu.oms.aioveu02OrderItem.model.vo.OmsOrderDetailVO;
import com.aioveu.oms.aioveu02OrderItem.model.vo.OmsOrderItemVO;
import com.aioveu.oms.aioveu03OrderDelivery.mapper.OmsOrderDeliveryMapper;
import com.aioveu.oms.aioveu03OrderDelivery.model.entity.OmsOrderDelivery;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.oms.aioveu02OrderItem.mapper.OmsOrderItemMapper;
import com.aioveu.oms.aioveu02OrderItem.model.entity.OmsOrderItem;
import com.aioveu.oms.aioveu02OrderItem.service.OmsOrderItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO  订单商品信息服务类
 * @Date  2026/1/8 19:39
 * @Param
 * @return
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl extends ServiceImpl<OmsOrderItemMapper, OmsOrderItem> implements OmsOrderItemService {

    private final OmsOrderItemConverter omsOrderItemConverter;

    private final OmsOrderMapper orderMapper;
    private final OmsOrderItemMapper orderItemMapper;
    private final OmsOrderDeliveryMapper orderDeliveryMapper;

    /**
     * 获取订单商品信息分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<OmsOrderItemVO>} 订单商品信息分页列表
     */
    @Override
    public IPage<OmsOrderItemVO> getOmsOrderItemPage(OmsOrderItemQuery queryParams) {
        Page<OmsOrderItemVO> pageVO = this.baseMapper.getOmsOrderItemPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取订单商品信息表单数据
     *
     * @param id 订单商品信息ID
     * @return 订单商品信息表单数据
     */
    @Override
    public OmsOrderItemForm getOmsOrderItemFormData(Long id) {
        OmsOrderItem entity = this.getById(id);
        return omsOrderItemConverter.toForm(entity);
    }

    /**
     * 新增订单商品信息
     *
     * @param formData 订单商品信息表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveOmsOrderItem(OmsOrderItemForm formData) {
        OmsOrderItem entity = omsOrderItemConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新订单商品信息
     *
     * @param id   订单商品信息ID
     * @param formData 订单商品信息表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateOmsOrderItem(Long id,OmsOrderItemForm formData) {
        OmsOrderItem entity = omsOrderItemConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除订单商品信息
     *
     * @param ids 订单商品信息ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteOmsOrderItems(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的订单商品信息数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }



    /**
     * 根据订单编号获取订单详情
     *
     * @param orderSn 订单编号
     * @param channel 渠道标识
     * @return 订单详情VO
     */
    @Override
    public OmsOrderDetailVO getOrderDetailBySn(String orderSn, Integer channel) {


        log.info("【获取订单详情】开始查询, orderSn={}, channel={}", orderSn, channel);


        // 1. 根据订单编号查询订单
        OmsOrder order = orderMapper.selectOne(
                new LambdaQueryWrapper<OmsOrder>()
                        .eq(OmsOrder::getOrderSn, orderSn)
                        .eq(OmsOrder::getDeleted, 0) // 未删除
        );

        if (order == null) {
            log.warn("【获取订单详情】订单不存在, orderSn={}", orderSn);
            return null;
        }

        // 2. 调用根据ID查询的方法
        return getOrderDetailById(order.getId(), channel);

    }


    /**
     * 根据订单ID获取订单详情
     *
     * @param orderId 订单ID
     * @param channel 渠道标识
     * @return 订单详情VO
     */
    @Override
    public OmsOrderDetailVO getOrderDetailById(Long orderId, Integer channel) {


        log.info("【获取订单详情】开始查询, orderId={}, channel={}", orderId, channel);


        // 1. 查询订单主表
        OmsOrder order = orderMapper.selectById(orderId);

        if (order == null) {
            log.warn("【获取订单详情】订单不存在, orderId={}", orderId);
            return null;
        }

        // 2. 构建订单详情VO
        OmsOrderDetailVO orderDetail = new OmsOrderDetailVO();
        // 2.1 复制订单基本信息
        BeanUtils.copyProperties(order, orderDetail);

        // 2.2 设置状态文本
        orderDetail.setStatusText(getOrderStatusText(order.getStatus()));
        orderDetail.setStatusDesc(getOrderStatusDesc(order.getStatus()));

        // 2.3 设置支付方式文本
        orderDetail.setPaymentMethodText(getPaymentMethodText(order.getPaymentMethod()));

        // 2.4 设置来源文本
        orderDetail.setSourceText(getSourceText(order.getSource()));

        // 3. 查询订单商品项
        List<OmsOrderItem> orderItems = orderItemMapper.selectList(
                new LambdaQueryWrapper<OmsOrderItem>()
                        .eq(OmsOrderItem::getOrderId, orderId)
                        .eq(OmsOrderItem::getDeleted, 0) // 未删除的
        );

        if (!CollectionUtils.isEmpty(orderItems)) {
            // 转换商品项VO
            List<OmsOrderDetailVO.OrderItemDetail> itemDetails = orderItems.stream()
                    .map(this::convertToOrderItemDetail)
                    .collect(Collectors.toList());
            orderDetail.setOrderItems(itemDetails);
        }


        // 4. 查询收货地址和物流信息
        OmsOrderDelivery delivery = orderDeliveryMapper.selectOne(
                new LambdaQueryWrapper<OmsOrderDelivery>()
                        .eq(OmsOrderDelivery::getOrderId, orderId)
                        .eq(OmsOrderDelivery::getDeleted, 0) // 未删除的
        );


        if (delivery != null) {
            // 复制收货信息
            orderDetail.setReceiverName(delivery.getReceiverName());
            orderDetail.setReceiverPhone(delivery.getReceiverPhone());
            orderDetail.setReceiverPostCode(delivery.getReceiverPostCode());
            orderDetail.setReceiverProvince(delivery.getReceiverProvince());
            orderDetail.setReceiverCity(delivery.getReceiverCity());
            orderDetail.setReceiverRegion(delivery.getReceiverRegion());
            orderDetail.setReceiverDetailAddress(delivery.getReceiverDetailAddress());

            // 拼接完整地址
            String fullAddress = delivery.getReceiverProvince() +
                    delivery.getReceiverCity() +
                    delivery.getReceiverRegion() +
                    delivery.getReceiverDetailAddress();
            orderDetail.setReceiverFullAddress(fullAddress);

            // 复制物流信息
            orderDetail.setDeliveryCompany(delivery.getDeliveryCompany());
            orderDetail.setDeliverySn(delivery.getDeliverySn());
            orderDetail.setDeliveryStatus(delivery.getDeliveryStatus());
            orderDetail.setDeliveryStatusText(getDeliveryStatusText(delivery.getDeliveryStatus()));
        }


        // 5. 设置操作权限
        setOperationPermissions(orderDetail, order);

        log.info("【获取订单详情】查询成功, orderId={}, orderSn={}, status={}",
                orderId, order.getOrderSn(), order.getStatus());
        return orderDetail;

    }

    /**
     * 转换订单商品项
     */
    private OmsOrderDetailVO.OrderItemDetail convertToOrderItemDetail(OmsOrderItem item) {
        OmsOrderDetailVO.OrderItemDetail itemDetail = new OmsOrderDetailVO.OrderItemDetail();
        BeanUtils.copyProperties(item, itemDetail);
        return itemDetail;
    }

    /**
     * 设置操作权限
     */
    private void setOperationPermissions(OmsOrderDetailVO orderDetail, OmsOrder order) {
        Integer status = order.getStatus();

        // 待付款订单
        if (status == 1) {
            orderDetail.setCanRefund(false);
            orderDetail.setCanReturn(false);
        }
        // 待发货订单
        else if (status == 2) {
            orderDetail.setCanRefund(true);  // 可以申请退款
            orderDetail.setCanReturn(false);
        }
        // 已发货订单
        else if (status == 3) {
            orderDetail.setCanRefund(true);  // 可以申请退款
            orderDetail.setCanReturn(true);  // 可以申请退货
        }
        // 已完成订单
        else if (status == 4) {
            // 检查是否在售后期限内（比如7天内）
            boolean inAfterSalePeriod = checkAfterSalePeriod(order.getReceiveTime());
            orderDetail.setCanRefund(inAfterSalePeriod);
            orderDetail.setCanReturn(inAfterSalePeriod);
        }
        // 已关闭/已取消订单（根据您的表结构，没有5、6状态，如果有的话可以在这里扩展）
        else {
            orderDetail.setCanRefund(false);
            orderDetail.setCanReturn(false);
        }

        // 是否可以重新购买（除了已关闭/已取消的订单都可以重新购买，这里简化处理）
        orderDetail.setCanRebuy(status >= 1 && status <= 4);

        // 是否已评价（根据评价时间判断）
        orderDetail.setIsCommented(order.getCommentTime() != null);
    }

    /**
     * 检查是否在售后期限内
     */
    private boolean checkAfterSalePeriod(Date receiveTime) {
        if (receiveTime == null) return false;

        long now = System.currentTimeMillis();
        long receiveTimeMillis = receiveTime.getTime();
        long sevenDays = 7L * 24 * 60 * 60 * 1000; // 7天

        return (now - receiveTimeMillis) <= sevenDays;
    }

    // ==================== 状态文本转换方法 ====================

    private String getOrderStatusText(Integer status) {
        switch (status) {
            case 0: return "待付款";
            case 1: return "待发货";
            case 2: return "已发货";
            case 3: return "已完成";
            case 4: return "已取消";
            case 5: return "售后中";
            default: return "未知";
        }
    }

    private String getOrderStatusDesc(Integer status) {
        switch (status) {
            case 0: return "等待买家付款";
            case 1: return "等待卖家发货";
            case 2: return "商品已发出";
            case 3: return "交易已完成";
            case 4: return "交易已取消";
            case 5: return "售后处理中";
            default: return "";
        }
    }

    private String getPaymentMethodText(Integer paymentMethod) {

        if (paymentMethod == null) {
            return "微信支付";  // 或者返回 "未支付"
        }

        switch (paymentMethod) {
            case 1: return "微信支付";
            case 2: return "支付宝支付";
            case 3: return "余额支付";
            case 4: return "微信app支付";
            default: return "未知";
        }
    }

    private String getSourceText(Integer source) {
        switch (source) {
            case 0: return "PC订单";
            case 1: return "app订单";
            default: return "未知";
        }
    }

    private String getDeliveryStatusText(Integer status) {
        switch (status) {
            case 0: return "运输中";
            case 1: return "已收货";
            default: return "未知";
        }
    }



}
