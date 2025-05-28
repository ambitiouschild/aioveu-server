package com.aioveu.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: fanxiaole
 * @date: 2025/3/8 17:25
 */
@Data
public class GradeTemplateVO {

    private String id;

    private String name;

    private Date startTime;

    private Date endTime;

    private Integer status;

    private String levelName;

    private String className;

    private String fieldNames;

    private String venueName;

    private Long gradeLevelId;

    private Long gradeClassroomId;

    private Integer timeType;

    private String dateList;

    private String timeRule;

    private String coachNames;

}
