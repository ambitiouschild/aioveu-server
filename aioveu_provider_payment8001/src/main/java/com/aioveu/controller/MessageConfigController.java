package com.aioveu.controller;

import com.aioveu.entity.MessageConfig;
import com.aioveu.service.MessageConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/02/28 0005 12:01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/message-config")
public class MessageConfigController {

    @Autowired
    private MessageConfigService messageConfigService;

    @GetMapping("/template-id")
    public List<String> getMiniAppTemplateId(@RequestParam Long storeId, @RequestParam String msgCodes) {
        return messageConfigService.getMiniAppTemplateIdByMsgCode(storeId, msgCodes);
    }

    @GetMapping("/list")
    public List<MessageConfig> getList(@RequestParam Long storeId, @RequestParam String msgCodes) {
        return messageConfigService.getList(storeId, msgCodes);
    }

    @PutMapping("/status")
    public boolean updateStatus(@RequestParam String id, @RequestParam Integer status) {
        return messageConfigService.updateStatus(id, status);
    }

}
