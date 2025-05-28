package com.aioveu.vo;

import lombok.Data;

import java.sql.Time;
import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/12/3 13:16
 */
@Data
public class FieldBookUserDetailVO {

    private String fieldName;

    private Date fieldDay;

    private Time startTime;

    private Time endTime;



}
