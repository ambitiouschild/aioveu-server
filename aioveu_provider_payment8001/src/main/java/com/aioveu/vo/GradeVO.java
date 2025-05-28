package com.aioveu.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: fanxiaole
 * @date: 2025/3/8 17:25
 */
@Data
public class GradeVO {

    private Long id;

    private String gradeTemplateId;
    private String couponTemplateIds;

    private String name;

    private Date startTime;

    private Date endTime;

    private String fieldNames;

    private String venueName;

    /**
     * 班级级别
     */
    private String levelName;

    private String storeName;

    private String storeAddress;

    /**
     * 教室
     */
    private String gradeClassroom;

    /**
     * 课程年龄段
     */
    private String gradeAge;

    /**
     * 报名人数
     */
    private Integer enrollNumber;

    /**
     * 限制人数
     */
    private Integer limitNumber;

    /**
     * 签到人数
     */
    private Integer signNumber;

    private Integer status;

    private String teachers;

    private String couponTemplates;

    private String explainReason;

    /**
     * 体验课预约订单
     */
    private Integer experienceCount;

}
