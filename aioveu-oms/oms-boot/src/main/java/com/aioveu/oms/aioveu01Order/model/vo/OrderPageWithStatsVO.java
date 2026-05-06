package com.aioveu.oms.aioveu01Order.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: TODO App-订单分页视图对象 包含统计数据
 * @Author: 雒世松
 * @Date: 2025/6/5 18:13
 * @param
 * @return:
 **/

@Schema(description ="App-订单分页视图对象 包含统计数据")
@Data
public class OrderPageWithStatsVO {


    @Schema(description = "订单列表")
    private List<OrderPageVO> list;

    @Schema(description = "当前页码")
    private Long pageNum;

    @Schema(description = "每页大小")
    private Long pageSize;

    @Schema(description = "总记录数")
    private Long total;

    @Schema(description = "总页数")
    private Long pages;

    @Schema(description = "是否有下一页")
    private Boolean hasNextPage;

    @Schema(description = "各状态订单数量")
    private Map<String, Integer> statusCounts = new HashMap<>();

    @Schema(description = "今日订单数")
    private Integer todayOrderCount = 0;

    @Schema(description = "待处理订单数")
    private Integer pendingCount = 0;

    @Schema(description = "今日收入（分）")
    private Long todayIncome = 0L;

}
