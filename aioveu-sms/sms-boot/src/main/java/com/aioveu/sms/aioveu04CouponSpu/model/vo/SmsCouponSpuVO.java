package com.aioveu.sms.aioveu04CouponSpu.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName: SmsCouponSpuVO
 * @Description TODO 优惠券适用的具体商品视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/12 12:15
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "优惠券适用的具体商品视图对象")
public class SmsCouponSpuVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    @Schema(description = "优惠券ID")
    private Long couponId;
    @Schema(description = "商品ID")
    private Long spuId;
}
