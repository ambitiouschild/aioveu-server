package com.aioveu.system.aioveu12WebSocket.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: ChatMessage
 * @Description TODO  系统消息体
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 17:47
 * @Version 1.0
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

    /**
     * 发送者
     */
    private String sender;

    /**
     * 消息内容
     */
    private String content;
}
