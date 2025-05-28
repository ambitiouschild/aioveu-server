package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.service.UserBalanceChangeService;
import com.aioveu.vo.UserBalanceChangeItemVO;
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
@RequestMapping("/api/v1/user-balance-change")
public class UserBalanceChangeController {

    @Autowired
    private UserBalanceChangeService userBalanceChangeService;

    @GetMapping("")
    public IPage<UserBalanceChangeItemVO> list(@RequestParam(required = false, defaultValue = "1") Integer page,
                                               @RequestParam(required = false, defaultValue = "10") Integer size,
                                               @RequestParam(required = false) Integer type,
                                               @RequestParam(required = false) Integer accountType,
                                               @RequestParam(required = false) Long userVipCardId,
                                               @RequestParam String userId) {
        return userBalanceChangeService.getAll(page, size, type, userId, accountType, userVipCardId);
    }

}
