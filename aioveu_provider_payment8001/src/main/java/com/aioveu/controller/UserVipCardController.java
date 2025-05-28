package com.aioveu.controller;

import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.entity.UserVipCard;
import com.aioveu.service.UserVipCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/user-vipcard")
public class UserVipCardController {

    @Autowired
    private UserVipCardService userVipCardService;

    @GetMapping("list/{companyId}")
    public List<UserVipCard> list(@PathVariable Long companyId, @RequestParam String userId) {
        return userVipCardService.getActiveList(companyId, userId);
    }

    @GetMapping("manage-list/{companyId}")
    public List<UserVipCard> manageList(@PathVariable Long companyId
            , @RequestParam(required = false)  String phone
            , @RequestParam(required = false)  String vipCardNo
            , @RequestParam(required = false) String username) {
        return userVipCardService.getManageList(companyId, phone, vipCardNo, username);
    }

    @GetMapping("/{userVipCardId}")
    public UserVipCard detail(@PathVariable Long userVipCardId) {
        return userVipCardService.getOneById(userVipCardId);
    }

    @PostMapping("")
    public void saveVipCard(@RequestBody UserVipCard userVipCard) {
        userVipCardService.saveUserVipCard(userVipCard, OauthUtils.getCurrentUserId());
    }

}
