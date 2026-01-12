package com.aioveu.sms.aioveu03CouponHistory.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: SmsCouponHistoryQuery
 * @Description TODO 优惠券领取/使用记录分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/12 11:56
 * @Version 1.0
 **/

@Schema(description ="优惠券领取/使用记录查询对象")
@Getter
@Setter
public class SmsCouponHistoryQuery extends BasePageQuery {

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
    @Schema(description = "订单ID")
    private Long orderId;
    @Schema(description = "订单号")
    private String orderSn;
}
