package com.aioveu.vo;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: fanxiaole
 * @date: 2025/3/8 17:25
 */
@Data
public class GradeTemplateDetailVO {

    private String id;

    private Integer status;

    private Long storeId;

    private String storeName;

    private String name;

    private Long gradeLevelId;

    private Long gradeClassroomId;

    private Long gradeAgeId;

    private String couponTemplateIds;

    private String coachIds;

    private String startDay;

    private String endDay;

    private String startTime;

    private String endTime;

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
     * 时间类型 默认0 自由时间 1每星期 2每月 3每天
     */
    private Integer timeType;

    private Boolean skipHoliday;

    private String remark;

    private String dateStr;

    private List<String> dateList;
    private Long venueId;
    private String fieldIds;
    private List<Long> fieldIdList;
    private Integer sharedVenue;

}
