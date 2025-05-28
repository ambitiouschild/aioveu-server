package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/2 0002 12:37
 */
@TableName("sport_grade")
@Data
public class Grade extends IdNameEntity {

    private Long storeId;

    private String gradeTemplateId;

    /**
     * 限制人数
     */
    private Integer limitNumber;

    private Date startTime;

    private Date endTime;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long gradeClassroomId;

    private String remark;

    /**
     * 是否超额 默认不可
     */
    private Boolean exceed;

    private Integer minNumber;

    private Integer sharedVenue;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long venueId;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String fieldIds;

    // status 默认1 正常 0 删除 2下架 3 结束 4 取消

}
