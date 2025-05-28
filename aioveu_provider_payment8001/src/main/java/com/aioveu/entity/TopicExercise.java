package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_topic_exercise")
@Data
public class TopicExercise extends IdEntity {

    @NotNull(message = "主题id不能为空")
    private Long topicId;

    @NotNull(message = "活动id不能为空")
    private Long exerciseId;

    private Long storeId;

    private Long categoryId;

    private Integer priority;

    // 6 下架

}
