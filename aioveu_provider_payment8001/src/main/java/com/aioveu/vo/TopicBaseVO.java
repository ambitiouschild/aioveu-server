package com.aioveu.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/10/3 0003 23:52
 */
@Data
public class TopicBaseVO extends IdNameVO {

    private String cover;

    /**
     * 已报名数量
     */
    private Integer enrollNumber;

    /**
     * 总的活动数
     */
    private Integer total;

    private String color;

    private Date endTime;
}
