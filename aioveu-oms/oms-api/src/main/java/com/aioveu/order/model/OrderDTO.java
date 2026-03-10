package com.aioveu.order.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @ClassName: OrderDTO
 * @Description TODO
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/10 13:59
 * @Version 1.0
 **/

@Data
@Accessors(chain = true)
public class OrderDTO {

    private OmsOrder order;

    private List<OmsOrderItem> orderItems;
}
