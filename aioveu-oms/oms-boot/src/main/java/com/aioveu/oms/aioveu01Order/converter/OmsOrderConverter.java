package com.aioveu.oms.aioveu01Order.converter;

import com.aioveu.oms.aioveu01Order.model.entity.OmsOrder;
import com.aioveu.oms.aioveu01Order.model.form.OmsOrderForm;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aioveu.oms.aioveu01Order.model.vo.OrderBO;
import com.aioveu.oms.aioveu05OrderPay.model.form.OrderSubmitForm;
import com.aioveu.oms.aioveu01Order.model.vo.OmsOrderPageVO;
import com.aioveu.oms.aioveu01Order.model.vo.OrderPageVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

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
                    expression = "java(com.aioveu.common.base.IBaseEnum.getLabelByValue(bo.getPaymentMethod(), com.aioveu.oms.aioveu01Order.enums.PaymentMethodEnum.class))"
            ),
            @Mapping(
                    target = "sourceLabel",
                    expression = "java(com.aioveu.common.base.IBaseEnum.getLabelByValue(bo.getSource(), com.aioveu.oms.aioveu01Order.enums.OrderSourceEnum.class))"
            ),
            @Mapping(
                    target = "statusLabel",
                    expression = "java(com.aioveu.common.base.IBaseEnum.getLabelByValue(bo.getStatus(), com.aioveu.oms.aioveu01Order.enums.OrderStatusEnum.class))"
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
                    expression = "java(com.aioveu.common.base.IBaseEnum.getLabelByValue(bo.getPaymentMethod(), com.aioveu.oms.aioveu01Order.enums.PaymentMethodEnum.class))"
            ),
            @Mapping(
                    target = "sourceLabel",
                    expression = "java(com.aioveu.common.base.IBaseEnum.getLabelByValue(bo.getSource(), com.aioveu.oms.aioveu01Order.enums.OrderSourceEnum.class))"
            ),
            @Mapping(
                    target = "statusLabel",
                    expression = "java(com.aioveu.common.base.IBaseEnum.getLabelByValue(bo.getStatus(), com.aioveu.oms.aioveu01Order.enums.OrderStatusEnum.class))"
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
    })
    OrderPageVO toVoPageForApp(OrderBO bo);



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