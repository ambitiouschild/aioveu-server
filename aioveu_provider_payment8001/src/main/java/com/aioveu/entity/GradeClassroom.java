package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description 班级教室
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_grade_classroom")
@Data
public class GradeClassroom extends IdNameEntity {

    private Long storeId;

    private Long venueId;

}
