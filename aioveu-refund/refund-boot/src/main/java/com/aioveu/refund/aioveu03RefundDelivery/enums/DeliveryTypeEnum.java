package com.aioveu.refund.aioveu03RefundDelivery.enums;

import com.aioveu.common.base.IBaseEnum;
import lombok.Getter;

/**
 * @ClassName: DeliveryTypeEnum
 * @Description TODO   物流类型枚举：1-买家发货 2-卖家发货 3-换货发货
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 15:56
 * @Version 1.0
 *
 *   DeliveryTypeEnum.Exchange_shipment.getValue()
 **/
public enum DeliveryTypeEnum implements IBaseEnum<Integer> {

    /**
     * 仅退款
     */
    Buyer_ships(1, "买家发货"),
    /**
     * 退货退款
     */
    Seller_shipment(2, "卖家发货"),
    /**
     * 换货
     */
    Exchange_shipment(3, "换货发货");


    DeliveryTypeEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    @Getter
    private Integer value;

    @Getter
    private String label;

}
