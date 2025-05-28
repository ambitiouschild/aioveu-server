package com.aioveu.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: fanxiaole
 * @date: 2025/3/8 17:25
 */
@Data
public class GradeEnrollUserDetailVO {

    private Long id;

    private String gradeName;

    private String coachNames;

    private String username;

    private Date startTime;

    private Date endTime;

    private String storeName;

    private String storeAddress;

    private String childName;

    private Integer childAge;

    private String phone;

    private Long storeId;

    /**
     * 固定约课Id
     */
    private Long fixedId;

    private Integer status;

    /**
     * 课评
     */
    private String evaluate;

    /**
     * 课评人
     */
    private String evaluateUser;

    /**
     * 教室
     */
    private String gradeClassroom;

    /**
     * 班级级别
     */
    private String gradeLevel;

    /**
     * 课程年龄段
     */
    private String gradeAge;

    private Integer timeType;

    private String dateList;

    private String fieldNames;

    private String venueName;
}
