package com.aioveu.dto;

import lombok.Data;

import java.util.Date;

/**
 * @description 活动会员卡信息
 * @author: 雒世松
 * @date: 2025/12/3 13:16
 */
@Data
public class ExerciseVipDTO {

    private String companyName;

    private String storeName;

    private Date fixedTime;

    private Integer receiveDay;

    private Long exerciseId;
}
