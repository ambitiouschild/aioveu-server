package com.aioveu.controller;

import com.aioveu.service.ConditionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/17 0017 18:25
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/condition")
public class ConditionRestController {

    @Autowired
    private ConditionService conditionService;

    @GetMapping("/exercise")
    public Map<String, Object> exerciseMenu(@RequestParam(value = "city", defaultValue = "上海市", required = false) String city){
        return conditionService.getExerciseMenu(city);
    }

    @GetMapping("/store")
    public Map<String, Object> storeMenu(@RequestParam(value = "city", defaultValue = "上海市", required = false) String city){
        return conditionService.getStoreMenu(city);
    }
}
