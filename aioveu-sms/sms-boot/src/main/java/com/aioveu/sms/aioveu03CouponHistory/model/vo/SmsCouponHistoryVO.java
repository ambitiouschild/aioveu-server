package com.aioveu.sms.aioveu03CouponHistory.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: SmsCouponHistoryVO
 * @Description TODO 优惠券领取/使用记录视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/12 11:56
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "优惠券领取/使用记录视图对象")
public class SmsCouponHistoryVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "优惠券领取/使用记录ID")
    private Long id;
    @Schema(description = "优惠券ID")
    private Long couponId;
    @Schema(description = "会员ID")
    private Long memberId;
    @Schema(description = "会员昵称")
    private String memberNickname;
    @Schema(description = "优惠券码")
    private String couponCode;
    @Schema(description = "获取类型(1：后台增删；2：主动领取)")
    private Integer getType;
    @Schema(description = "状态(0：未使用；1：已使用；2：已过期)")
    private Integer status;
    @Schema(description = "使用时间")
    private LocalDateTime useTime;
    @Schema(description = "订单ID")
    private Long orderId;
    @Schema(description = "订单号")
    private String orderSn;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
