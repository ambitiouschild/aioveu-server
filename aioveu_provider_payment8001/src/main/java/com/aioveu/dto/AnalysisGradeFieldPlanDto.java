package com.aioveu.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author： 统计教练班级使用场地总时长、总场地金额
 * @Date： 2025/1/1 15:51
 * @Describe：
 */
@Data
public class AnalysisGradeFieldPlanDto {
    private Long gradeId;
    private Long fieldPlanId;
    private BigDecimal coachPrice;
    private Date gradeStartTime;
    private Date gradeEndTime;
    private Date fieldPlanStartTime;
    private Date fieldPlanEndTime;
    private Integer sharedVenue;
}
