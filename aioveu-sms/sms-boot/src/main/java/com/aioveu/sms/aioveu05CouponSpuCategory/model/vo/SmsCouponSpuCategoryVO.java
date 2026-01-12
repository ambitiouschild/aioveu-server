package com.aioveu.sms.aioveu05CouponSpuCategory.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName: SmsCouponSpuCategoryVO
 * @Description TODO 优惠券适用的具体分类视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/12 13:08
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "优惠券适用的具体分类视图对象")
public class SmsCouponSpuCategoryVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    @Schema(description = "优惠券ID")
    private Long couponId;
    @Schema(description = "商品分类ID")
    private Long categoryId;
}
