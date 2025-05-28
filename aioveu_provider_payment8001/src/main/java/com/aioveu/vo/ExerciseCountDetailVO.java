package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/24 19:29
 */
@Data
public class ExerciseCountDetailVO {

    private Long id;

    private Long categoryId;

    private String categoryCode;

    private String name;

    private String address;


    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 备注
     */
    private String remark;

    private BigDecimal originalPrice;

    private BigDecimal price;

    private BigDecimal vipPrice;

    private Date startTime;

    private Date endTime;

    private Integer status;

    private Integer limitNumber;


    private String description;

    /**
     * 已报名人数
     */
    private Integer enrollCount;

    /**
     * 已报名人 取 6个
     */
    private List<UserEnterVO> enrollUserList;


    /**
     * 活动实际
     */
    private String exerciseTime;

    private Date exerciseStartTime;

    /**
     * 活动实际结束时间
     */
    private Date exerciseEndTime;

    /**
     * 报名时间
     */
    private String enrollTime;

    private Long storeId;

    private String createUsername;

    private String head;

}
