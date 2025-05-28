package com.aioveu.controller;

import com.aioveu.entity.ChargingOption;
import com.aioveu.service.ChargingOptionService;
import com.aioveu.vo.IdNameCodeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/api/v1/charging-option")
public class ChargingOptionController {

    @Autowired
    private ChargingOptionService chargingOptionService;

    @GetMapping("")
    public List<IdNameCodeVO> list() {
        return chargingOptionService.getChargingOptionList();
    }

    @GetMapping("/{code}")
    public ChargingOption getByCode(@PathVariable("code") String code) {
        return chargingOptionService.getByCode(code);
    }



}
