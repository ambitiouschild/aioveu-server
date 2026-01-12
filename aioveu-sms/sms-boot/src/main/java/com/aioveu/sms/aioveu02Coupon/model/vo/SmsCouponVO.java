package com.aioveu.sms.aioveu02Coupon.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * @Description: TODO 优惠券分页视图对象
 * @Author: 雒世松
 * @Date: 2025/6/5 18:48
 * @param
 * @return:
 **/

@Schema(description = "优惠券分页视图对象")
@Data
public class SmsCouponVO implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description="ID")
    private Long id;

    @Schema(description="优惠券名称")
    private String name;

    @Schema(description = "优惠券类型(1-满减券;2-直减券;3-折扣券)")
    private Integer type;

    @Schema(description="优惠券码")
    private String code;

    @Schema(description = "使用平台(0-全平台;1-APP;2-PC)")
    private String platformLabel;

    //---------------------------------
    @Schema(description="优惠券类型标签")
    private String typeLabel;
    //---------------------------------
    @Schema(description="优惠券面值")
    private String faceValueLabel;


    @Schema(description = "优惠券面值类型")
    private Integer faceValueType;
    @Schema(description = "优惠券面值(分)")
    private Long faceValue;

    @Schema(description = "折扣")
    private BigDecimal discount;

    //---------------------------------
    @Schema(description="使用门槛")
    private String minPointLabel;

    @Schema(description = "使用门槛(0:无门槛)")
    private Long minPoint;
    @Schema(description = "每人限领张数(-1-无限制)")
    private Integer perLimit;

    //---------------------------------
    @Schema(description="优惠券有效期")
    private String validityPeriodLabel;

    @Schema(description = "有效期类型(1:自领取时起有效天数;2:有效起止时间)")
    private Integer validityPeriodType;
    @Schema(description = "有效期天数")
    private Integer validityDays;
    @Schema(description = "有效期起始时间")
    private LocalDateTime validityBeginTime;
    @Schema(description = "有效期截止时间")
    private LocalDateTime validityEndTime;
    @Schema(description = "应用范围(0-全场通用;1-指定商品分类;2-指定商品)")
    private Integer applicationScope;
    @Schema(description = "发行量(-1-无限制)")
    private Integer circulation;
    @Schema(description = "已领取的优惠券数量(统计)")
    private Integer receivedCount;
    @Schema(description = "已使用的优惠券数量(统计)")
    private Integer usedCount;

    @Schema(description="使用说明")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime updateTime;
    @Schema(description = "修改时间")
    private LocalDateTime createTime;
    @Schema(description = "逻辑删除标识(0-正常;1-删除)")
    private Integer deleted;

}
