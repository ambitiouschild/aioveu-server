package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.ChatHistory;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface ChatHistoryService extends IService<ChatHistory> {

    /**
     * 通过会话id获取历史聊天记录
     * @param sessionId
     * @return
     */
    ChatHistory getChatHistory(String sessionId);


}
