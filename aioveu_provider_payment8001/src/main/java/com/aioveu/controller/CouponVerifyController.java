package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.service.CouponVerifyService;
import com.aioveu.vo.CouponVerifyItemVO;
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
@RequestMapping("/api/v1/coupon-verify")
public class CouponVerifyController {

    @Autowired
    private CouponVerifyService couponVerifyService;

    @GetMapping("")
    public IPage<CouponVerifyItemVO> list(@RequestParam(required = false, defaultValue = "1") Integer page,
                                          @RequestParam(required = false, defaultValue = "10") Integer size,
                                          @RequestParam(required = false) Long storeId,
                                          @RequestParam(required = false) String date,
                                          @RequestParam(required = false) String phone) {
        return couponVerifyService.pageList(page, size, storeId, date, phone);
    }

    



}
