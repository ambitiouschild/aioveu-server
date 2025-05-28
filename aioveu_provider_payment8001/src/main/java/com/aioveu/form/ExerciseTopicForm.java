package com.aioveu.form;

import com.aioveu.entity.ExercisePushRewardConfig;
import lombok.Data;

import java.util.List;

/**
 * @description 灵活课包
 * @author: 雒世松
 * @date: 2025/8/27 0027 6:20
 */
@Data
public class ExerciseTopicForm {

    private Long categoryId;

    /**
     * 奖励配置
     */
    private List<ExercisePushRewardConfig> configList;
}
