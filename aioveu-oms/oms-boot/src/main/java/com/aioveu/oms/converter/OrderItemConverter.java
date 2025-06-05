
package com.aioveu.oms.converter;

import com.aioveu.oms.model.entity.OmsOrderItem;
import com.aioveu.oms.model.form.OrderSubmitForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @Description: TODO 订单商品明细对象转化器
 * @Author: 雒世松
 * @Date: 2025/6/5 18:07
 * @param
 * @return:
 **/

@Mapper(componentModel = "spring")
public interface OrderItemConverter {

    @Mappings({
            @Mapping(target = "totalAmount", expression = "java(item.getPrice() * item.getQuantity())"),
    })
    OmsOrderItem item2Entity(OrderSubmitForm.OrderItem item);

    List<OmsOrderItem> item2Entity(List<OrderSubmitForm.OrderItem> list);

}