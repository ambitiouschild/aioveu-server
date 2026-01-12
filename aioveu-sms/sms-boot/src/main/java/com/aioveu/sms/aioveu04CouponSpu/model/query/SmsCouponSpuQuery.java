package com.aioveu.sms.aioveu04CouponSpu.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: SmsCouponSpuQuery
 * @Description TODO 优惠券适用的具体商品分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/12 12:14
 * @Version 1.0
 **/

@Schema(description ="优惠券适用的具体商品查询对象")
@Getter
@Setter
public class SmsCouponSpuQuery extends BasePageQuery {

    @Schema(description = "优惠券ID")
    private Long couponId;
    @Schema(description = "商品ID")
    private Long spuId;
}
