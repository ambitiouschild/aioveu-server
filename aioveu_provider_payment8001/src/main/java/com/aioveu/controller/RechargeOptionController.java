package com.aioveu.controller;

import com.aioveu.entity.RechargeOption;
import com.aioveu.service.RechargeOptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/9/5 0005 12:01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/recharge-option")
public class RechargeOptionController {

    @Autowired
    private RechargeOptionService rechargeOptionService;

    @GetMapping("")
    public List<RechargeOption> list() {
        return rechargeOptionService.getRechargeList();
    }


}
