package com.aioveu.sms.aioveu04CouponSpu.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName: SmsCouponSpuForm
 * @Description TODO 优惠券适用的具体商品表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/12 12:14
 * @Version 1.0
 **/

@Getter
@Setter
@Schema(description = "优惠券适用的具体商品表单对象")
public class SmsCouponSpuForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "优惠券ID")
    @NotNull(message = "优惠券ID不能为空")
    private Long couponId;

    @Schema(description = "商品ID")
    @NotNull(message = "商品ID不能为空")
    private Long spuId;
}
