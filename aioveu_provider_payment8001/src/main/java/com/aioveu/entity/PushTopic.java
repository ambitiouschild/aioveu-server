package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_push_topic")
@Data
public class PushTopic extends IdEntity{

    @NotNull(message = "主题id不能为空")
    private Long topicId;

    @NotEmpty(message = "用户id不能为空")
    private String userId;


}
