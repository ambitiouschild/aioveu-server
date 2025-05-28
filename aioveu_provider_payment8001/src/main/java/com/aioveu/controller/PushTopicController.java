package com.aioveu.controller;

import com.aioveu.entity.PushTopic;
import com.aioveu.service.PushTopicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/push")
public class PushTopicController {

    @Autowired
    private PushTopicService pushTopicService;

    @PostMapping("dispatch")
    public boolean dispatchPushUser(
            @RequestBody PushTopic pushTopic
            ){
        return pushTopicService.create(pushTopic);
    }
}
