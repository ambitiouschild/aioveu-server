package com.aioveu.oms.model.dto;

import com.aioveu.oms.model.entity.OmsOrder;
import com.aioveu.oms.model.entity.OmsOrderItem;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class OrderDTO {

    private OmsOrder order;

    private List<OmsOrderItem> orderItems;

}
