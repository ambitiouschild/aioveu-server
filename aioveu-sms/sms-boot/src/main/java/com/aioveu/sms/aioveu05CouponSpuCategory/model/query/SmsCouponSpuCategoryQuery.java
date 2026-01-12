package com.aioveu.sms.aioveu05CouponSpuCategory.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: SmsCouponSpuCategoryQuery
 * @Description TODO 优惠券适用的具体分类分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/12 13:08
 * @Version 1.0
 **/

@Schema(description ="优惠券适用的具体分类查询对象")
@Getter
@Setter
public class SmsCouponSpuCategoryQuery extends BasePageQuery {

    @Schema(description = "优惠券ID")
    private Long couponId;
    @Schema(description = "商品分类ID")
    private Long categoryId;
}
