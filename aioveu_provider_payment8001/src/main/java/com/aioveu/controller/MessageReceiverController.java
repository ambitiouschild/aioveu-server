package com.aioveu.controller;

import com.aioveu.enums.DataStatus;
import com.aioveu.form.MessageReceiverForm;
import com.aioveu.service.MessageReceiverService;
import com.aioveu.vo.MessageReceiverVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 消息接受人
 * @Author： yao
 * @Date： 2025/4/27 19:43
 * @Describe：
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/message-receiver")
public class MessageReceiverController {

    @Autowired
    private MessageReceiverService messageReceiveService;

    @GetMapping("/list/{msgConfigId}")
    public List<MessageReceiverVO> list(@RequestParam Long storeId, @PathVariable Long msgConfigId){
        return  messageReceiveService.getVoList(storeId, msgConfigId);
    }

    @PutMapping("/delete/{id}")
    public Boolean delete(@PathVariable Long id){
        return  messageReceiveService.updateStatus(id, DataStatus.DELETE.getCode());
    }

    @PostMapping("/create")
    public Boolean create(@Valid @RequestBody MessageReceiverForm form){
        return  messageReceiveService.add(form);
    }
}
