package com.aioveu.dto;

import lombok.Data;

import java.sql.Time;
import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2023/5/5 0005 23:16
 */
@Data
public class FieldBookUserDTO {

    private Date fieldDay;

    private Time startTime;

    private Time endTime;



    private String username;

    private String phone;

    private Integer gender;


    private String venueName;

    private String fieldName;


}
