package com.aioveu.system.aioveu10Log.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @ClassName: VisitStatsBO
 * @Description TODO  访问量统计业务对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 18:10
 * @Version 1.0
 **/

@Getter
@Setter
public class VisitStatsBO {

    @Schema(description = "今日访问量 (PV)")
    private Integer todayCount;

    @Schema(description = "累计访问量 ")
    private Integer totalCount;

    @Schema(description = "页面访问量增长率")
    private BigDecimal growthRate;
}
