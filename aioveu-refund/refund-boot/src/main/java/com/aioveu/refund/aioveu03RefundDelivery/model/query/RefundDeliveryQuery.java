package com.aioveu.refund.aioveu03RefundDelivery.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: RefundDeliveryQuery
 * @Description TODO 退款物流信息（用于退货）分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 17:59
 * @Version 1.0
 **/

@Schema(description ="退款物流信息（用于退货）查询对象")
@Getter
@Setter
public class RefundDeliveryQuery extends BasePageQuery {

    @Schema(description = "退款申请ID")
    private Long refundId;
    @Schema(description = "物流类型：1-买家发货 2-卖家发货 3-换货发货")
    private Integer deliveryType;
    @Schema(description = "物流公司")
    private String deliveryCompany;
    @Schema(description = "物流单号")
    private String deliverySn;
    @Schema(description = "收货人姓名")
    private String receiverName;
    @Schema(description = "收货人电话")
    private String receiverPhone;
}
