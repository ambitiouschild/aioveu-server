package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_grade_enroll_user")
@Data
public class GradeEnrollUser extends IdEntity {

    private Long gradeId;

    private String userId;

    private String username;

    private String childName;

    private Integer childAge;

    private String phone;

    /**
     * 约课类型 默认 0约课 1体验课
     */
    private Integer appointmentType;

    private Long storeId;

    private Long companyId;

    // status 0 删除 1 正常 2 (用户)已取消  3 已完成  4 系统取消 8 商户取消

}
