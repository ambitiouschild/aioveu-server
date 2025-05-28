package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/2 0002 12:37
 */
@TableName("sport_exercise_count_template")
@Data
public class ExerciseCountTemplate extends StringIdNameEntity {

    private Long companyId;

    private Long storeId;

    private String image;

    private BigDecimal originalPrice;

    private BigDecimal price;

    private BigDecimal hourPrice;

    /**
     * 会员折扣
     */
    private Double vipDiscount;

    private String description;

    private String process;

    private String requirement;

    /**
     * 活动开始报名时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDay;

    /**
     * 活动结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDay;

    /**
     * 活动实际开始时间
     */
    private Time exerciseStartTime;

    /**
     * 活动实际结束时间
     */
    private Time exerciseEndTime;

    /**
     * 限制人数
     */
    private Integer limitNumber;

    /**
     * 报名人数
     */
    private Integer enrollNumber;

    private Long venueId;

    private String venueFieldIds;

    private String venueFieldNames;

    private String remark;

    /**
     * 创建人
     */
    private String createUserId;

    private String createUsername;

    /**
     * 时间类型 默认0 自由时间 1每星期 2每月 3每天
     */
    private Integer timeType;

    private String dateList;

    private Boolean skipHoliday;

    @TableField(exist = false)
    private String storeName;

    // status 1 正常 0 删除 2 待审核  4 审核失败

}
