package com.aioveu.form;

import com.alibaba.dashscope.common.Message;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/3 18:11
 */
@Data
public class ChatForm {

    @NotEmpty(message = "会话id不能为空")
    private String sessionId;

    @NotEmpty(message = "用户id不能为空")
    private String userId;

    /**
     * 聊天类型 1 公众号 0 会话 2 微信
     */
    private Integer chatType;

    @NotNull(message = "消息不能为空")
    private Message message;

    private Long companyId;

}
