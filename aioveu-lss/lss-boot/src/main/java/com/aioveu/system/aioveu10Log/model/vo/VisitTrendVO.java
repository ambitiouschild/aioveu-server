package com.aioveu.system.aioveu10Log.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @ClassName: VisitTrendVO
 * @Description TODO  访问趋势VO
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 17:05
 * @Version 1.0
 **/

@Schema(description = "访问趋势VO")
@Getter
@Setter
public class VisitTrendVO {

    @Schema(description = "日期列表")
    private List<String> dates;

    @Schema(description = "浏览量(PV)")
    private List<Integer> pvList;

    @Schema(description = "IP数")
    private List<Integer> ipList;
}
