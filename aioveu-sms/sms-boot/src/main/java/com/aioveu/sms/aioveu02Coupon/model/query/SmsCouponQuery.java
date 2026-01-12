package com.aioveu.sms.aioveu02Coupon.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: TODO 优惠券分页查询对象
 * @Author: 雒世松
 * @Date: 2025/6/5 18:47
 * @param
 * @return:
 **/

@Schema(description = "优惠券分页查询对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsCouponQuery extends BasePageQuery {

    @Schema(description="状态")
    private Integer status;

    @Schema(description = "优惠券名称")
    private String name;
    @Schema(description = "优惠券类型(1-满减券;2-直减券;3-折扣券)")
    private Integer type;
    @Schema(description = "优惠券码")
    private String code;
    @Schema(description = "使用平台(0-全平台;1-APP;2-PC)")
    private Integer platform;
    @Schema(description = "优惠券面值类型")
    private Integer faceValueType;
    @Schema(description = "使用门槛(0:无门槛)")
    private Long minPoint;
    @Schema(description = "每人限领张数(-1-无限制)")
    private Integer perLimit;
    @Schema(description = "有效期类型(1:自领取时起有效天数;2:有效起止时间)")
    private Integer validityPeriodType;
    @Schema(description = "有效期天数")
    private Integer validityDays;
    @Schema(description = "应用范围(0-全场通用;1-指定商品分类;2-指定商品)")
    private Integer applicationScope;
    @Schema(description = "已领取的优惠券数量(统计)")
    private Integer receivedCount;
    @Schema(description = "逻辑删除标识(0-正常;1-删除)")
    private Integer deleted;
}
