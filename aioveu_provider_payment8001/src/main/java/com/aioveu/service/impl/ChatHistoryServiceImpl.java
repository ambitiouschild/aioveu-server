package com.aioveu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.ChatHistoryDao;
import com.aioveu.entity.ChatHistory;
import com.aioveu.service.ChatHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class ChatHistoryServiceImpl extends ServiceImpl<ChatHistoryDao, ChatHistory> implements ChatHistoryService {


    @Override
    public ChatHistory getChatHistory(String sessionId) {
        return getById(sessionId);
    }
}
