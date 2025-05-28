package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @description 活动展示记录
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_exercise_show_record")
@Data
public class ExerciseShowRecord extends IdEntity {

    @NotNull(message = "活动id不能为空")
    private Long exerciseId;

    private String userId;

    private Long topicId;

}
