package com.aioveu.refund.aioveu03RefundDelivery.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: RefundDeliveryVO
 * @Description TODO 退款物流信息（用于退货）视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 18:00
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "退款物流信息（用于退货）视图对象")
public class RefundDeliveryVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;
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
    @Schema(description = "收货地址")
    private String receiverAddress;
    @Schema(description = "发货时间")
    private LocalDateTime deliveryTime;
    @Schema(description = "收货时间")
    private LocalDateTime receiveTime;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
    @Schema(description = "逻辑删除")
    private Integer deleted;
}
