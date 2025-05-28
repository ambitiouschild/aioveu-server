package com.aioveu.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description 灵活课包
 * @author: 雒世松
 * @date: 2025/8/27 0027 6:20
 */
@Data
public class TopicExercisePrePayForm {

    @NotNull(message = "主题id不能为空")
    private Long topicId;

    @NotEmpty(message = "userId不能为空")
    private String userId;

    List<Long> exerciseIdList;

}
