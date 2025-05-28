package com.aioveu.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author 用户约课信息
 * @description
 * @date 2025/1/22 17:06
 */
@Data
public class GradeEnrollUserDTO {

    private Long id;

    private Integer status;

    private String name;

    private Date startTime;

    private Date endTime;

}
