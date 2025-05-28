package com.aioveu.controller;

import com.aioveu.form.ChargingRechargeOrderForm;
import com.aioveu.service.ChargingRechargeOrderService;
import com.aioveu.vo.WxMaPayVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/9/5 0005 12:01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/charging-recharge-order")
public class ChargingRechargeOrderController {

    @Autowired
    private ChargingRechargeOrderService chargingRechargeOrderService;

    @PostMapping("")
    public WxMaPayVO create(@Valid ChargingRechargeOrderForm form) {
        return chargingRechargeOrderService.create(form);
    }

    @PutMapping("/{orderId}")
    public Boolean checkPayStatus(@PathVariable String orderId) {
        return chargingRechargeOrderService.checkPayStatus(orderId);
    }

}
