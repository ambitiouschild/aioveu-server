package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description 班级等级
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_grade_file")
@Data
public class GradeFile extends IdNameEntity {

    private Long gradeId;

    private String filePath;

}
