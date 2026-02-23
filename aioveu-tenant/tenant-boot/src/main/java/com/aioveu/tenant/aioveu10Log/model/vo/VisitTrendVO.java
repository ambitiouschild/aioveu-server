package com.aioveu.tenant.aioveu10Log.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: VisitTrendVO
 * @Description TODO 访问趋势Vo
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 20:04
 * @Version 1.0
 **/
@Schema(description = "访问趋势Vo")
@Data
public class VisitTrendVO {

    @Schema(description = "日期列表")
    private List<String> dates;

    @Schema(description = "浏览量(PV)")
    private List<Integer> pvList;

    @Schema(description = "IP数")
    private List<Integer> ipList;
}
