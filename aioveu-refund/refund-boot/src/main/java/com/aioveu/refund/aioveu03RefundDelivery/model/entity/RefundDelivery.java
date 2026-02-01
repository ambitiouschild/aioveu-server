package com.aioveu.refund.aioveu03RefundDelivery.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @ClassName: RefundDelivery
 * @Description TODO 退款物流信息（用于退货）实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 17:57
 * @Version 1.0
 **/

@Getter
@Setter
@TableName("refund_delivery")
public class RefundDelivery extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 退款申请ID
     */
    private Long refundId;
    /**
     * 物流类型：1-买家发货 2-卖家发货 3-换货发货
     */
    private Integer deliveryType;
    /**
     * 物流公司
     */
    private String deliveryCompany;
    /**
     * 物流单号
     */
    private String deliverySn;
    /**
     * 收货人姓名
     */
    private String receiverName;
    /**
     * 收货人电话
     */
    private String receiverPhone;
    /**
     * 收货地址
     */
    private String receiverAddress;
    /**
     * 发货时间
     */
    private LocalDateTime deliveryTime;
    /**
     * 收货时间
     */
    private LocalDateTime receiveTime;
    /**
     * 逻辑删除
     */
    private Integer deleted;
}
