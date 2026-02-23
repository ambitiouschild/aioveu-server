package com.aioveu.tenant.aioveu10Log.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName: VisitStatsVO
 * @Description TODO 访问量统计视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 20:03
 * @Version 1.0
 **/
@Schema(description = "访问量统计视图对象")
@Data
public class VisitStatsVO {

    @Schema(description = "今日独立访客数 (UV)")
    private Integer todayUvCount;

    @Schema(description = "累计独立访客数 (UV)")
    private Integer totalUvCount;

    @Schema(description = "独立访客增长率")
    private BigDecimal uvGrowthRate;

    @Schema(description = "今日页面浏览量 (PV)")
    private Integer todayPvCount;

    @Schema(description = "累计页面浏览量 (PV)")
    private Integer totalPvCount;

    @Schema(description = "页面浏览量增长率")
    private BigDecimal pvGrowthRate;
}
