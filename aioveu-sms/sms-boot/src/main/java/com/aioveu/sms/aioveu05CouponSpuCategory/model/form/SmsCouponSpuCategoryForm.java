package com.aioveu.sms.aioveu05CouponSpuCategory.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName: SmsCouponSpuCategoryForm
 * @Description TODO 优惠券适用的具体分类表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/12 13:07
 * @Version 1.0
 **/

@Getter
@Setter
@Schema(description = "优惠券适用的具体分类表单对象")
public class SmsCouponSpuCategoryForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "优惠券ID")
    @NotNull(message = "优惠券ID不能为空")
    private Long couponId;

    @Schema(description = "商品分类ID")
    @NotNull(message = "商品分类ID不能为空")
    private Long categoryId;

}
