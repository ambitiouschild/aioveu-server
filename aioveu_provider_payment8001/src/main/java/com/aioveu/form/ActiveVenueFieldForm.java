package com.aioveu.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/3/6 0006 22:57
 */
@Data
public class ActiveVenueFieldForm {

    private String gradeTemplateId;

    private Long gradeId;

    @NotNull(message = "开始日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDay;

    @NotNull(message = "结束日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDay;

    @NotEmpty(message = "开始时间不能为空")
    private String startTime;

    @NotEmpty(message = "结束时间不能为空")
    private String endTime;

    /**
     * 时间类型 默认0 自由时间 1每星期 2每月 3每天
     */
    @NotNull(message = "时间类型不能为空")
    private Integer timeType;

    private Boolean skipHoliday;

    /**
     * 时间列表
     */
    private List<String> dateList;

    @NotNull(message = "场馆id不能为空")
    private Long venueId;

    private Integer sharedVenue;
}
