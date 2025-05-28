package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Time;
import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/2 0002 12:37
 */
@TableName("sport_grade_template")
@Data
public class GradeTemplate extends StringIdNameEntity {

    private Long storeId;

    /**
     * 限制人数
     */
    private Integer limitNumber;

    /**
     * 是否超额 默认不可
     */
    private Boolean exceed;

    /**
     * 最少人数
     */
    private Integer minNumber;

    /**
     * 消耗课时
     */
    private Integer classHour;

    /**
     * 上课教室id
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long gradeClassroomId;

    private Long gradeLevelId;

    private Long gradeAgeId;

    private Date startDay;

    private Date endDay;

    private Time startTime;

    private Time endTime;

    private String remark;

    /**
     * 时间类型 默认0 自由时间 1每星期 2每月 3每天
     */
    private Integer timeType;

    private String dateList;

    private Boolean skipHoliday;

    /**
     * 优惠券模板id集合
     */
    private String couponTemplateIds;

    /**
     * 教练/老师 id 集合
     */
    private String coachIds;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long venueId;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String fieldIds;

    private Integer sharedVenue;

    // status 1 正常 2 下架

}
