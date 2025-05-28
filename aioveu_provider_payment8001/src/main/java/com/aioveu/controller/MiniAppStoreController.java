package com.aioveu.controller;

import com.aioveu.service.MiniAppStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/9/5 0005 12:01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/mini-app-store")
public class MiniAppStoreController {

    @Autowired
    private MiniAppStoreService miniAppStoreService;

    @GetMapping("/{appId}")
    public Long list(@PathVariable String appId) {
        return miniAppStoreService.getDefaultStoreId(appId);
    }

    



}
