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
public class GradeUserDetailVO {

    private Long id;

    private String gradeTemplateId;

    private String name;

    private Date startTime;

    private Date endTime;

    private Integer limitNumber;

    private String remark;

    private String storeName;

    private String address;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

    private String tel;

    private String gradeClassroomName;

    private String gradeLevelName;

    private String gradeAgeName;

    /**
     * 已报名人 取 6个
     */
    private List<UserEnterVO> enrollUserList;

    /**
     * 已报名人数
     */
    private Integer enrollCount;

    /**
     * 教师列表
     */
    private List<StoreSimpleCoachVO> coachList;

    /**
     * 课程列表
     */
    private List<ExerciseSimpleVO> courseList;

    private String fieldNames;

    private String venueName;

    private Long storeId;

}
