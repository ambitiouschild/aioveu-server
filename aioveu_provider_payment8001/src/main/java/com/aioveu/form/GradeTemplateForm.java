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
public class GradeTemplateForm {

    private String id;

    @NotEmpty(message = "名称不能为空")
    private String name;

    @NotNull(message = "店铺不能为空")
    private Long storeId;

    /**
     * 限制人数
     */
    private Integer limitNumber;

    /**
     * 是否超额 默认不可
     */
    private Boolean exceed;

    /**
     * 最少人数
     */
    private Integer minNumber;

    /**
     * 消耗课时
     */
    private Integer classHour;

    /**
     * 上课教室id
     */
    private Long gradeClassroomId;

    private Long gradeLevelId;

    private Long gradeAgeId;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDay;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDay;

    @NotEmpty(message = "开始时间不能为空")
    private String startTime;

    @NotEmpty(message = "结束时间不能为空")
    private String endTime;

    private String remark;

    /**
     * 时间类型 默认0 自由时间 1每星期 2每月 3每天
     */
    private Integer timeType;

    private Boolean skipHoliday;

    /**
     * 课程id集合
     */
    @NotEmpty(message = "课程券不能为空")
    private String couponTemplateIds;

    /**
     * 教练/老师 id 集合
     */
    @NotEmpty(message = "老师不能为空")
    private String coachIds;

    /**
     * 时间列表
     */
    private List<String> dateList;

    /**
     * 模板状态  1正常 2下架
     */
    private Integer status;

    private Integer sharedVenue;

    @NotNull(message = "场馆不能为空")
    private Long venueId;

    private List<Long> fieldIdList;
}
