package com.aioveu.vo;

import com.aioveu.entity.JoinExerciseRule;
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
public class ExerciseVO {

    private Long id;

    private Long categoryId;

    private String categoryCode;

    private List<String> imageList;

    private List<String> imageDetailList;

    private String video;

    private String name;

    private String address;

    private String tags;

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

    /**
     * 单个用户的限制人数 0 不限制
     */
    private Integer singleNumber;

    private String description;

    private String target;

    private String suitablePeople;

    private String requirement;

    private String qa;

    private String careful;

    /**
     * 已报名人数
     */
    private Integer enrollCount;

    /**
     * 已报名人 取 6个
     */
    private List<UserEnterVO> enrollUserList;

    private String process;

    private Boolean needAddress;

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

    /**
     * 是否需要定位
     */
    private Boolean needLocation;

    /**
     * 优惠券列表
     */
    private List<ExerciseCouponSimpleVO> exerciseCouponList;

    /**
     * 协议模板地址
     */
    private String agreementTemplate;

    private Long storeId;

    /**
     * 拼单规则
     */
    private List<JoinExerciseRule> joinExerciseRule;

}
