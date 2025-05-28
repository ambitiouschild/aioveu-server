package com.aioveu.form;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/3/6 0006 22:57
 */
@Data
public class GradeEnrollUserForm implements Serializable {

    @NotNull(message = "班级id不能为空")
    private Long gradeId;

    @NotEmpty(message = "用户不能为空!")
    private String userId;

    private String username;

    @NotEmpty(message = "孩子姓名不能为空!")
    private String childName;

    @NotNull(message = "孩子年龄不能为空!")
    @Min(message = "孩子年龄填写错误", value = 3)
    @Max(value = 70, message = "孩子年龄填写错误")
    private Integer childAge;

    @NotEmpty(message = "手机号码不能为空!")
    private String phone;

    /**
     * 约课类型 默认 0约课 1体验课
     */
    private Integer appointmentType;

    /**
     * 是否固定
     */
    private Boolean fixed;

    /**
     * 历史预约
     */
    private boolean historyAppointment;

}
