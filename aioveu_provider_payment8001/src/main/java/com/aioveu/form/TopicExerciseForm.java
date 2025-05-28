package com.aioveu.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @description 灵活课包
 * @author: 雒世松
 * @date: 2025/8/27 0027 6:20
 */
@Data
public class TopicExerciseForm {

    @NotNull(message = "主题id不能为空")
    private Long topicId;

    private Long categoryId;

    private String userId;

    @Min(value = 1)
    @NotNull(message = "页码不能为空")
    private Integer page;

    @Min(value = 1)
    @NotNull(message = "页数不能为空")
    private Integer size;

    private Double latitude;

    private Double longitude;

}
