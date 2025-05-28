package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.service.CashOrderService;
import com.aioveu.vo.CashOrderItemVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/9/5 0005 12:01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/cash-order")
public class CashOrderController {

    @Autowired
    private CashOrderService cashOrderService;

    @GetMapping("")
    public IPage<CashOrderItemVo> orderList(@RequestParam(required = false, defaultValue = "1") Integer page,
                                            @RequestParam(required = false, defaultValue = "10") Integer size) {
        return cashOrderService.orderList(page, size, OauthUtils.getCurrentUsername());
    }

}
