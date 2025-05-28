package com.aioveu.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: fanxiaole
 * @date: 2025/3/8 17:25
 */
@Data
public class GradeEnrollUserItemVO {

    private Long id;

    private String gradeName;

    private String username;

    private Date startTime;

    private Date endTime;

    private String storeName;

    private Integer status;

    private String phone;

    private Long fixedId;

}
