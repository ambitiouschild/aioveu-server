package com.aioveu.controller;

import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.constant.SportConstant;
import com.aioveu.feign.form.WxMpUserForm;
import com.aioveu.service.UserMpSubscribeService;
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
@RequestMapping("/api/v1/user-mp-subscribe")
public class UserMpSubscribeController {

    @Autowired
    private UserMpSubscribeService userMpSubscribeService;

    @PostMapping("")
    public Boolean subscribeEvent(@RequestBody WxMpUserForm form) {
        return userMpSubscribeService.subscribe(form);
    }


    @GetMapping("/status")
    public Boolean getIsSubscribe() {
        return userMpSubscribeService.getIsSubscribe(OauthUtils.getCurrentUserId(), SportConstant.QU_SHU_WECHAT_MP);
    }

}
