package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.aioveu.entity.handler.MessageListTypeHandler;
import lombok.Data;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/2 23:43
 */
@TableName(value = "sport_chat_history", autoResultMap = true)
@Data
public class ChatHistory extends StringIdEntity {

    private String userId;

    @TableField(typeHandler = MessageListTypeHandler.class)
    private List<AiMessage> messages;

}
