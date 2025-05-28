package com.aioveu.controller;

import com.aioveu.entity.MessageOption;
import com.aioveu.service.MessageOptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 消息通知选项
 * @Author： yao
 * @Date： 2025/4/27 19:43
 * @Describe：
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/message-option")
public class MessageOptionController {

    @Autowired
    private MessageOptionService messageOptionService;

    @GetMapping("/list/{storeId}")
    public List<MessageOption> list(@PathVariable String storeId){
        return  messageOptionService.getListByStoreId(storeId);
    }
}
