package com.aioveu.sms.aioveu03CouponHistory.model.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: SmsCouponHistoryForm
 * @Description TODO 优惠券领取/使用记录表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/12 11:55
 * @Version 1.0
 **/

@Getter
@Setter
@Schema(description = "优惠券领取/使用记录表单对象")
public class SmsCouponHistoryForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "优惠券领取/使用记录ID")
    private Long id;

    @Schema(description = "优惠券ID")
    @NotNull(message = "优惠券ID不能为空")
    private Long couponId;

    @Schema(description = "会员ID")
    @NotNull(message = "会员ID不能为空")
    private Long memberId;

    @Schema(description = "会员昵称")
    @Size(max=50, message="会员昵称长度不能超过50个字符")
    private String memberNickname;

    @Schema(description = "优惠券码")
    @NotBlank(message = "优惠券码不能为空")
    @Size(max=32, message="优惠券码长度不能超过32个字符")
    private String couponCode;

    @Schema(description = "获取类型(1：后台增删；2：主动领取)")
    @NotNull(message = "获取类型(1：后台增删；2：主动领取)不能为空")
    private Integer getType;

    @Schema(description = "状态(0：未使用；1：已使用；2：已过期)")
    private Integer status;

    @Schema(description = "使用时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime useTime;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "订单号")
    @Size(max=32, message="订单号长度不能超过32个字符")
    private String orderSn;
}
