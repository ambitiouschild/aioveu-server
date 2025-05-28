package com.aioveu.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/10/16 0016 15:14
 */
@Data
public class PushExerciseItemVO extends IdNameVO {

    private Integer status;

    private Date createDate;

    private String topicName;




}

