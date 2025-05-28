package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @description 班级取消记录
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_grade_cancel_record")
@Data
public class GradeCancelRecord extends IdEntity {

    @NotNull(message = "班级id不能为空")
    private Long gradeId;

    private String userId;

    @NotEmpty(message = "取消说明不能为空")
    private String explainReason;

    @TableField(exist = false)
    private Long companyId;

    @TableField(exist = false)
    private String userName;
}
