package com.aioveu.controller;

import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.service.RechargeOrderService;
import com.aioveu.vo.WxMaPayVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/9/5 0005 12:01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/recharge-order")
public class RechargeOrderController {

    @Autowired
    private RechargeOrderService rechargeOrderService;


    @PostMapping("/{id}")
    public WxMaPayVO recharge(@PathVariable Long id) {
        return rechargeOrderService.createRechargeOrder(OauthUtils.getCurrentUsername(), id);
    }

    @PutMapping("/{id}")
    public Boolean checkPayStatus(@PathVariable String id) {
        return rechargeOrderService.updateOrder2Success(id, null);
    }

}
