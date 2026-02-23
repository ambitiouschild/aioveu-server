package com.aioveu.tenant.aioveu10Log.controller;

import com.aioveu.common.result.Result;
import com.aioveu.tenant.aioveu10Log.model.vo.VisitStatsVO;
import com.aioveu.tenant.aioveu10Log.model.vo.VisitTrendVO;
import com.aioveu.tenant.aioveu10Log.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * @ClassName: StatisticsController
 * @Description TODO 统计分析控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/23 14:45
 * @Version 1.0
 **/
@Tag(name = "12.统计分析")
@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final LogService logService;

    @Operation(summary = "访问趋势统计")
    @GetMapping("/visits/trend")
    public Result<VisitTrendVO> getVisitTrend(
            @Parameter(description = "开始时间", example = "2024-01-01") @RequestParam String startDate,
            @Parameter(description = "结束时间", example = "2024-12-31") @RequestParam String endDate
    ) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        VisitTrendVO data = logService.getVisitTrend(start, end);
        return Result.success(data);
    }

    @Operation(summary = "访问概览统计")
    @GetMapping("/visits/overview")
    public Result<VisitStatsVO> getVisitOverview() {
        VisitStatsVO result = logService.getVisitStats();
        return Result.success(result);
    }
}
