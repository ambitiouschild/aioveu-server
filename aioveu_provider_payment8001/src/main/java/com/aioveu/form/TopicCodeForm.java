package com.aioveu.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/10 0010 21:43
 */
@Data
public class TopicCodeForm {

    @NotNull(message = "主题id不能为空")
    private Long topicId;

    @NotEmpty(message = "用户id不能为空")
    private String userId;

    private Integer runStep;

    @NotNull(message = "维度不能为空")
    private Double latitude;

    @NotNull(message = "经度不能为空")
    private Double longitude;

}
