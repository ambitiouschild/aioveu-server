package com.aioveu.tool;

import lombok.Data;

@Data
public class OrderTool {

    private String orderId;

    public String getOrderById(String orderId) {
        if (orderId.equals("order_111111")) {
            return "订单: 笔记本电脑, 订单日期: 2024年12月10日";
        } else {
            return "订单号不对";
        }
    }

}
