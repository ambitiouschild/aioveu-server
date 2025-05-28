package com.aioveu.form;

import com.aioveu.feign.form.WeChatAccountChatForm;
import lombok.Data;

/**
 * @description 个人微信机器人聊天
 * @author: 雒世松
 * @date: 2025/1/29 23:57
 */
@Data
public class WeChatUserChatForm extends WeChatAccountChatForm {

    private Long companyId;



}
