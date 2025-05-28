package com.aioveu.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/10 0010 21:43
 */
@Data
public class TopicOrderDetailForm {

    @NotNull(message = "主题id不能为空")
    private Long topicId;

    @NotEmpty(message = "userId不能为空")
    private String userId;

    /**
     * 地推人的用户id
     */
    private String shareUserId;

    @NotNull(message = "活动id不能为空")
    private Long productId;

}
