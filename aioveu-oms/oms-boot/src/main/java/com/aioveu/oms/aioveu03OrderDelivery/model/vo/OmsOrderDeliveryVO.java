package com.aioveu.oms.aioveu03OrderDelivery.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: OmsOrderDeliveryVO
 * @Description TODO  订单物流记录视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/8 20:18
 * @Version 1.0
 **/

//Serializable 是Java中的一个标记接口(Marker Interface),位于java.io包下,
// 它没有任何抽象方法,仅起到 “标记” 作用 —— 告诉JVM:“这个类的对象可以被序列化”。
@Getter
@Setter
@Schema( description = "订单物流记录视图对象")
public class OmsOrderDeliveryVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;
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
    @Schema(description = "收货人邮编")
    private String receiverPostCode;
    @Schema(description = "省份/直辖市")
    private String receiverProvince;
    @Schema(description = "城市")
    private String receiverCity;
    @Schema(description = "区")
    private String receiverRegion;
    @Schema(description = "详细地址")
    private String receiverDetailAddress;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "物流状态【0->运输中；1->已收货】")
    private Integer deliveryStatus;
    @Schema(description = "发货时间")
    private LocalDateTime deliveryTime;
    @Schema(description = "确认收货时间")
    private LocalDateTime receiveTime;
    @Schema(description = "逻辑删除【0->正常；1->已删除】")
    private Integer deleted;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "修改时间")
    private LocalDateTime updateTime;

}
