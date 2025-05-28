package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description 班级用户签到课评
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_grade_cancel_options")
@Data
public class GradeCancelOptions extends IdNameEntity {

    private Integer couponExtendDays;

    private Integer status;

    private Long companyId;

    @TableField(exist = false)
    private String appId;

}
