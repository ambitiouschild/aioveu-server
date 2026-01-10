package com.aioveu.oms.aioveu03OrderDelivery.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: OmsOrderDeliveryQuery
 * @Description TODO  订单物流记录分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/8 20:18
 * @Version 1.0
 **/

@Schema(description ="订单物流记录查询对象")
@Getter
@Setter
public class OmsOrderDeliveryQuery extends BasePageQuery {

    @Schema(description = "订单id")
    private Long orderId;
    @Schema(description = "物流公司(配送方式)")
    private String deliveryCompany;
    @Schema(description = "物流单号")
    private String deliverySn;
    @Schema(description = "收货人姓名")
    private String receiverName;
    @Schema(description = "收货人电话")
    private String receiverPhone;
    @Schema(description = "物流状态【0->运输中；1->已收货】")
    private Integer deliveryStatus;
}
