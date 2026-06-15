package com.aioveu.oms.aioveu01Order.converter;

import com.aioveu.oms.aioveu01Order.model.entity.OmsOrder;
import com.aioveu.order.model.aioveu01Order.form.OmsOrderForm;
import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aioveu.oms.aioveu01Order.model.vo.OrderBO;
import com.aioveu.oms.aioveu05OrderPay.model.form.OrderSubmitForm;
import com.aioveu.oms.aioveu01Order.model.vo.OmsOrderPageVO;
import com.aioveu.oms.aioveu01Order.model.vo.OrderPageVO;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: TODO 订单对象转化器
 * @Author: 雒世松
 * @Date: 2025/6/5 18:07
 * @param
 * @return:
 **/

@Mapper(componentModel = "spring")
public interface OmsOrderConverter {


    // 改名：管理后台的
    @Mappings({
            @Mapping(target = "orderSn", source = "orderToken"),
            @Mapping(target = "totalQuantity",
                    expression = "java(orderSubmitForm.getOrderItems().stream().map(OrderSubmitForm.OrderItem::getQuantity).reduce(0, Integer::sum))"),
            @Mapping(target = "totalAmount",
                    expression = "java(orderSubmitForm.getOrderItems().stream().map(item -> item.getPrice() * item.getQuantity()).reduce(0L, Long::sum))"),
            @Mapping(target = "source", expression = "java(orderSubmitForm.getOrderSource().getValue())"),
    })
    OmsOrder form2Entity(OrderSubmitForm orderSubmitForm);

    @Mappings({
            @Mapping(
                    target = "paymentMethodLabel",
                    expression = "java(bo.getPaymentMethod() != null ? bo.getPaymentMethod().getLabel() : \"未知\")"
            ),
            @Mapping(
                    target = "sourceLabel",
                    expression = "java(bo.getSource() != null ? bo.getSource().getLabel() : \"未知\")"
            ),
            @Mapping(
                    target = "statusLabel",
                    expression = "java(bo.getSource() != null ? bo.getSource().getLabel() : \"未知\")"
            ),
            @Mapping(
                    target = "orderItems",
                    source = "orderItems"
            ),
    })
    OmsOrderPageVO toVoPage(OrderBO bo);

    Page<OmsOrderPageVO> toVoPage(Page<OrderBO> boPage);


    // 改名：管理后台的
    OmsOrderPageVO.OrderItem toVoPageOrderItem(OrderBO.OrderItem orderItem);


    @Mappings({
            @Mapping(
                    target = "paymentMethodLabel",
                    expression = "java(bo.getPaymentMethod() != null ? bo.getPaymentMethod().getLabel() : \"未知\")"
            ),
            @Mapping(
                    target = "sourceLabel",
                    expression = "java(bo.getSource() != null ? bo.getSource().getLabel() : \"未知\")"
            ),
            @Mapping(
                    target = "statusLabel",
                    expression = "java(bo.getStatus() != null ? bo.getStatus().getLabel() : \"未知\")"
            ),
            // 关键：明确指定商品列表的转换方法
      /*      MapStruct 的自动行为：
            如果字段名完全一致，MapStruct 会按字段名自动映射
        如果有对应的转换方法，MapStruct 不一定会自动调用*/
            @Mapping(
                    target = "orderItems",
                    expression = "java(convertToOrderItemVOList(bo.getOrderItems()))"
            ),
            // 添加这两个映射
            @Mapping(target = "spuName", source = "spuName"),
            @Mapping(target = "picUrl", source = "picUrl"),
            // 物流信息字段映射
            @Mapping(target = "receiverName", source = "orderDelivery.receiverName"),
            @Mapping(target = "receiverPhone", source = "orderDelivery.receiverPhone"),
            @Mapping(target = "deliveryCompany", source = "orderDelivery.deliveryCompany"),
            @Mapping(target = "deliverySn", source = "orderDelivery.deliverySn"),
            @Mapping(target = "deliveryStatus", source = "orderDelivery.deliveryStatus"),
//            // 完整地址需要通过表达式拼接
//            @Mapping(
//                    target = "fullAddress",
//                    expression = "java(buildFullAddress(bo.getOrderDelivery()))"
//            )
    })
    OrderPageVO toVoPageForApp(OrderBO bo);

//=================================================
    /**
     * 映射后处理
     */
    @AfterMapping
    default void afterMapping(OrderBO bo, @MappingTarget OrderPageVO vo) {
        // 处理完整地址
        if (bo != null && bo.getOrderDelivery() != null) {
            OrderBO.OrderDelivery delivery = bo.getOrderDelivery();
            String fullAddress = buildFullAddress(delivery);
            vo.setFullAddress(fullAddress);
        }

        // 如果需要，可以在这里处理其他字段
        // 例如：设置物流状态对应的文字描述
        if (vo.getDeliveryStatus() != null) {
            String statusLabel = convertDeliveryStatus(vo.getDeliveryStatus());
            vo.setDeliveryStatusLabel(statusLabel);
        }
    }

    /**
     * 构建完整地址
     */
    default String buildFullAddress(OrderBO.OrderDelivery delivery) {
        if (delivery == null) {
            return null;
        }

        String province = delivery.getReceiverProvince();
        String city = delivery.getReceiverCity();
        String region = delivery.getReceiverRegion();
        String detail = delivery.getReceiverDetailAddress();

        StringBuilder sb = new StringBuilder();
        if (StringUtils.hasText(province)) {
            sb.append(province);
        }
        if (StringUtils.hasText(city)) {
            sb.append(city);
        }
        if (StringUtils.hasText(region)) {
            sb.append(region);
        }
        if (StringUtils.hasText(detail)) {
            sb.append(detail);
        }

        return sb.length() > 0 ? sb.toString() : null;
    }

    /**
     * 转换物流状态为文字描述
     */
    default String convertDeliveryStatus(Integer status) {
        if (status == null) {
            return null;
        }
        switch (status) {
            case 0: return "运输中";
            case 1: return "已收货";
            default: return "未知状态";
        }
    }

//=================================================

    // 分页转换使用手动方法
    default Page<OrderPageVO> toVoPageForApp(Page<OrderBO> boPage) {
        if (boPage == null) {
            return null;
        }

        Page<OrderPageVO> voPage = new Page<>();
        voPage.setCurrent(boPage.getCurrent());
        voPage.setSize(boPage.getSize());
        voPage.setTotal(boPage.getTotal());
        voPage.setPages(boPage.getPages());

        if (boPage.getRecords() != null && !boPage.getRecords().isEmpty()) {
            List<OrderPageVO> voList = boPage.getRecords().stream()
                    .map(this::toVoPageForApp)
                    .collect(Collectors.toList());
            voPage.setRecords(voList);
        } else {
            voPage.setRecords(new ArrayList<>());
        }

        return voPage;
    }


    // 商品项转换 - 如果字段名完全一致，可以不写 @Mappings
    // MapStruct 会自动按字段名映射
    // 商品项转换方法


    OrderPageVO.OrderItem toVoPageOrderItemForApp(OrderBO.OrderItem orderItem);


    // 商品列表转换方法
    default List<OrderPageVO.OrderItem> convertToOrderItemVOList(List<OrderBO.OrderItem> boItems) {
        if (boItems == null) {
            return null;
        }

        return boItems.stream()
                .map(this::toVoPageOrderItemForApp)
                .collect(Collectors.toList());
    }

    OmsOrderForm toForm(OmsOrder entity);

    OmsOrder toEntity(OmsOrderForm formData);
}