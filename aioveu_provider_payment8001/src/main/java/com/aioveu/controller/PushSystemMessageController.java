package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.entity.PushSystemMessage;
import com.aioveu.service.PushSystemMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/7 0007 23:13
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/push-system-message")
public class PushSystemMessageController {

    @Autowired
    private PushSystemMessageService pushSystemMessageService;

    @GetMapping("/new")
    public String newest() {
        return pushSystemMessageService.newest();
    }

    @GetMapping("/user-list")
    public IPage<PushSystemMessage> userList(@RequestParam(required = false, defaultValue = "1") Integer page,
                                             @RequestParam(required = false, defaultValue = "10") Integer size) {
        return pushSystemMessageService.userList(page, size, OauthUtils.getCurrentUsername());
    }

    @GetMapping("/read/{id}")
    public Boolean read(@PathVariable Long id) {
        return pushSystemMessageService.read(id, OauthUtils.getCurrentUsername());
    }

    @PostMapping("/create")
    public Boolean createOrUpdateMessage(@RequestBody PushSystemMessage pushSystemMessage){
        return pushSystemMessageService.createOrUpdate(pushSystemMessage);
    }

    @GetMapping("/{id}")
    public Boolean lowMessage(@PathVariable Long id){
        return pushSystemMessageService.low(id);
    }

    @GetMapping("/find")
    public IPage<PushSystemMessage> findMessage(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long id
    ){
        return pushSystemMessageService.findMessage(page,size,name,id);
    }

}
