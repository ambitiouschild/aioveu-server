package com.aioveu.tenant.aioveu10Log.model.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName: VisitStatsBO
 * @Description TODO 访问量统计业务对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 20:12
 * @Version 1.0
 **/
@Data
@Schema(description = "访问量统计业务对象")
public class VisitStatsBO {

    @Schema(description = "今日访问量 (PV)")
    private Integer todayCount;

    @Schema(description = "累计访问量 ")
    private Integer totalCount;

    @Schema(description = "页面访问量增长率")
    private BigDecimal growthRate;
}
