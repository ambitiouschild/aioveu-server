package com.aioveu.sms.aioveu02Coupon.model.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description: TODO 优惠券表单对象
 * @Author: 雒世松
 * @Date: 2025/6/5 18:47
 * @param
 * @return:
 **/

@Schema(description = "优惠券表单对象")
@Data
public class SmsCouponForm implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description="ID")
    private Long id;

    @Schema(description="优惠券名称")
    @NotBlank(message = "优惠券名称不能为空")
    @Size(max=64, message="优惠券名称长度不能超过64个字符")
    private String name;

    @Schema(description="优惠券类型(1:满减券;2:直减券;3:折扣券)")
    @NotNull(message = "优惠券类型(1-满减券;2-直减券;3-折扣券)不能为空")
    private Integer type;


    @Schema(description = "优惠券码")
    @NotBlank(message = "优惠券码不能为空")
    @Size(max=32, message="优惠券码长度不能超过32个字符")
    private String code;

    @Schema(description = "使用平台(0-全平台;1-APP;2-PC)")
    @NotNull(message = "使用平台(0-全平台;1-APP;2-PC)不能为空")
    private Integer platform;

    @Schema(description="优惠券面值类型((1:金额;2:折扣)")
    @NotNull(message = "优惠券面值类型不能为空")
    private Integer faceValueType;

    @Schema(description="优惠券面值金额(单位:分)")
    private Long faceValue;

    @Schema(description="优惠券折扣")
    private BigDecimal discount;


    @Schema(description = "使用门槛(0:无门槛)")
    @NotNull(message = "使用门槛(0:无门槛)不能为空")
    private Long minPoint;


    @Schema(description="每人限领张数(-1:不限制)")
    @NotNull(message = "每人限领张数(-1-无限制)不能为空")
    private Integer perLimit;

    @Schema(description="有效期类型(1:日期范围;2:固定天数)")
    private Integer validityPeriodType;

    @Schema(description="自领取之日起有效天数")
    private Integer validityDays;

    @Schema(description="有效期开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date validityBeginTime;

    @Schema(description="有效期截止时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date validityEndTime;

    @Schema(description="应用范围(0:全场通用;1:指定商品分类;2:指定商品)")
    private Integer applicationScope;

    @Schema(description = "发行量(-1-无限制)")
    private Integer circulation;

    @Schema(description = "已领取的优惠券数量(统计)")
    private Integer receivedCount;

    @Schema(description = "已使用的优惠券数量(统计)")
    private Integer usedCount;

    @Schema(description="备注")
    @Size(max=255, message="备注长度不能超过255个字符")
    private String remark;

    @Schema(description = "逻辑删除标识(0-正常;1-删除)")
    private Integer deleted;

    @Schema(description="优惠券适用商品分类ID集合")
    private List<Long> spuCategoryIds;

    @Schema(description="优惠券适用商品列表")
    private List<Long> spuIds;
}