package com.aioveu.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: fanxiaole
 * @date: 2025/3/8 17:25
 */
@Data
public class GradeDetailVO {

    private Long id;
    private Integer timeType;

    private String gradeTemplateId;

    private String name;

    private String coachNames;

    private Date startTime;

    private Date endTime;

    private Long gradeClassroomId;

    /**
     * 是否超额 默认不可
     */
    private Boolean exceed;

    private String remark;

    /**
     * 限制人数
     */
    private Integer limitNumber;

    /**
     * 报名人数
     */
    private int enrollNumber;

    private Integer minNumber;

    private Integer status;

    private List<BaseNameVO> teacherList;

    private List<GradeCouponVO> couponTemplateList;
    private Long venueId;
    private String fieldIds;
    private List<Long> fieldIdList;
    private Integer sharedVenue;

}
