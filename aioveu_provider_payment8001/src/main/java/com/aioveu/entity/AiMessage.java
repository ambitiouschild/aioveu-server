package com.aioveu.entity;

import com.alibaba.dashscope.common.MessageContentText;
import com.alibaba.dashscope.tools.ToolCallFunction;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/14 22:16
 */
@Data
public class AiMessage implements Serializable {

    private String role;

    private String content;

    private List<ToolCallFunction> toolCalls;

    private String toolCallId;

    private String name;

    private List<MessageContentText> contents;

}
