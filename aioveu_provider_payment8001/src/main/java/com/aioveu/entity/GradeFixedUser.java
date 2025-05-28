package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_grade_fixed_user")
@Data
public class GradeFixedUser extends IdEntity {

    private String gradeTemplateId;

    private String userId;

    private String username;

    private String childName;

    private Integer childAge;

    private String phone;

}
