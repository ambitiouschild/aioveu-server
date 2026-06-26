package com.aioveu.oms.aioveu01Order.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: OrderStatisticsVO
 * @Description TODO 订单统计数据
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/26 17:51
 * @Version 1.0
 **/
@Schema(description ="App-订单统计数据")
@Data
public class OrderStatisticsVO {

    /** 状态统计（key = 枚举名） */
    private Map<String, Integer> statusCounts = new HashMap<>();

    /** 今日订单数 */
    private Integer todayOrderCount = 0;

    /** 待处理订单数（PAID） */
    private Integer pendingCount = 0;

    /** 今日收入（单位：分） */
    private Long todayIncome = 0L;

    /** 总订单数（当前条件） */
    private Integer total = 0;
}
