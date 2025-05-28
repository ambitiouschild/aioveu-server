package com.aioveu.dto;

import com.aioveu.entity.Order;
import lombok.Data;

@Data
//订单状态实体
public class OrderStatusVo {

    //订单状态
    private Integer status;

    //订单数量
    private Integer size;

    public OrderStatusVo(Order order) {
        this.status = order.getStatus();
        this.size = Integer.parseInt(order.getAddressId().toString());
    }
}
