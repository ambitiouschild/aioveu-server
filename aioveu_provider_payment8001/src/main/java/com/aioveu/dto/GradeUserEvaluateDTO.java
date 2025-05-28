package com.aioveu.dto;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: fanxiaole
 * @date: 2025/9/19 22:25
 */
@Data
public class GradeUserEvaluateDTO {

    private String evaluate;

    private String username;

    private String grade;

    private Date startTime;

    private Date endTime;

    private String teacher;

}
