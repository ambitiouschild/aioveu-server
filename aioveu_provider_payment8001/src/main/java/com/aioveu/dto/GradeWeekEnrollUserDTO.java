package com.aioveu.dto;

import lombok.Data;

import java.util.Date;

/**
 * @description: 用户预约课程信息
 * @author: fanxiaole
 * @date: 2025/9/19 22:25
 */
@Data
public class GradeWeekEnrollUserDTO {

    private String name;

    private Date startTime;

    private String userId;

    private String username;

    private String childName;

    private String phone;

    private Long storeId;

    private Long companyId;

    private String storeName;

}
