package com.aioveu.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: fanxiaole
 * @date: 2025/3/8 17:25
 */
@Data
public class GradeEnrollUserSimpleVO {

    private Long id;

    private String username;

    private String childName;

    private Integer childAge;

    private Date createDate;


    private Integer status;

    private String phone;

    /**
     * 约课类型 默认 0约课 1体验课
     */
    private Integer appointmentType;

    /**
     * 1 已签到 2 已课评
     */
    private Integer actionStatus;

    private String evaluate;

    /**
     * 签到人姓名
     */
    private String signUserName;

    /**
     * 是否预约体验 1 是，0 否
     */
    private Integer experience;

}
