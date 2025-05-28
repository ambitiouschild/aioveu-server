package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.Message;
import com.aioveu.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/9/5 0005 12:01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("")
    public boolean create(@RequestBody Message message){
        return messageService.create(message);
    }

    @GetMapping("")
    public IPage<Message> list(@RequestParam(required = false, defaultValue = "1") Integer page,
                                   @RequestParam(required = false, defaultValue = "10") Integer size,
                                   @RequestParam Long storeId,
                                   @RequestParam(required = false, defaultValue = "1") Integer msgType) {
        return messageService.getList(page, size, storeId, msgType);
    }

    @PutMapping("/read-all")
    public boolean readAll(@RequestParam Long storeId, @RequestParam Integer msgType){
        return messageService.readAll(storeId, msgType);
    }

    @PutMapping("/{id}")
    public boolean check(@PathVariable Long id) {
        return messageService.read(id);
    }

    @GetMapping("/number/{storeId}")
    public int getUnreadNumber(@PathVariable Long storeId, @RequestParam(required = false, defaultValue = "1") Integer msgType) {
        return messageService.getStoreUnreadMessageNumber(storeId, msgType);
    }

    @PostMapping("/menu-number/{storeId}")
    public Map<String, Integer> getMenuUnreadNumber(@PathVariable Long storeId, @RequestBody List<String> menuCodeList) {
        return messageService.getMenuUnreadNumber(storeId, menuCodeList);
    }

}
