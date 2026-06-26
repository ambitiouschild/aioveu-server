package com.aioveu.oms.aioveu01Order.model.query;

import com.aioveu.common.base.BasePageQuery;
import com.aioveu.common.enums.oms.OrderStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Description: TODO 订单分页查询对象
 * @Author: 雒世松
 * @Date: 2025/6/5 18:12
 * @param
 * @return:
 **/

@EqualsAndHashCode(callSuper = true)
@Schema(description ="订单分页查询对象")
@Data
public class OrderPageQuery extends BasePageQuery {

    /**
     * 关键字(订单编号/商品名称/会员姓名/会员手机号)
     */
    @Schema(description="关键字(订单编号/商品名称/会员姓名/会员手机号)")
    private String keyword;  // 改为单数，匹配前端

    @Schema(description = "订单号")
    private String orderSn;

    @Schema(description = "用户手机号")
    private String userPhone;

    @Schema(description = "用户昵称")
    private String userNickname;

    @Schema(description = "商品名称/货号")
    private String productKeyword;

    /**
     * 订单状态
     */
    @Schema(description="订单状态：-1-全部，0-待支付，1-待发货，2-已发货，3-已完成，4-已取消")
    /** null = 全部 */
    private OrderStatusEnum status;


    /*
        TODO    这是一个 Jackson 反序列化 的问题。错误信息显示：
                Cannot deserialize value of type java.time.LocalDateTime from String "2026-05-06 00:00:00"
                这是因为前端传过来的时间字符串格式是 "2026-05-06 00:00:00"（空格分隔），
                而 Jackson 默认期望的 LocalDateTime格式是 ISO-8601 格式（"2026-05-06T00:00:00"）。
    * */
    /**
     * 开始时间
     */
    @Schema(description = "开始时间(yyyy-MM-dd)",example = "2023-10-01")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  // 修正pattern
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")  // 添加时区
    private LocalDateTime startTime;

    /**
     * 截止时间
     */
    @Schema(description = "截止时间(yyyy-MM-dd)",example = "2025-10-01")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  // 修正pattern
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")  // 添加时区
    private LocalDateTime endTime;

    @Schema(description = "订单类型")
    private Integer orderType;

    @Schema(description = "支付方式")
    private Integer payType;

    @Schema(description = "配送方式")
    private Integer deliveryType;

    /**
     * 最小金额
     */
    @Schema(description = "最小金额")
    private BigDecimal minAmount;

    /**
     * 最大金额
     */
    @Schema(description = "最大金额")
    private BigDecimal maxAmount;

    /**
     * 订单来源(1=微信小程序；2=APP,3=H5)
     */
    @Schema(description = "订单来源")
    private Integer source;

}
