package com.aioveu.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/10/3 0003 23:52
 */
@Data
public class TopicItemVO extends IdNameVO {

    private String categoryName;

    private Integer status;

    private Date startTime;

    private Date endTime;

}
