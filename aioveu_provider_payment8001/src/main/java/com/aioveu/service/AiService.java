package com.aioveu.service;

import com.aioveu.form.ChatForm;

/**
 * @author xlfan10
 * @description
 * @date 2025/3/4 12:49
 */
public interface AiService {

    /**
     * 持续对话
     * @param chatForm
     * @return
     */
    String chatAsk(ChatForm chatForm);

}
