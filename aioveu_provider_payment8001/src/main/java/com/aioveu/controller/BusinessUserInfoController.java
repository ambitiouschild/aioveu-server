package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.service.BusinessUserInfoService;
import com.aioveu.vo.StoreUserPublicInfoVO;
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
@RequestMapping("/api/v1/business-user-info")
public class BusinessUserInfoController {

    @Autowired
    private BusinessUserInfoService businessUserInfoService;


    @GetMapping("")
    public IPage<StoreUserPublicInfoVO> list(@RequestParam (required = false, defaultValue = "1") Integer page,
                                             @RequestParam (required = false, defaultValue = "10") Integer size) {
        return businessUserInfoService.getList(page, size, OauthUtils.getCurrentUsername());
    }

    @PostMapping("/active")
    public boolean active(@RequestParam Long id) {
        return businessUserInfoService.active(id, OauthUtils.getCurrentUsername());
    }

    @PutMapping("/invalid/{id}")
    public boolean invalid(@PathVariable Long id) {
        return businessUserInfoService.phoneInvalid(id, OauthUtils.getCurrentUsername());
    }

    @GetMapping("invalid")
    public IPage<StoreUserPublicInfoVO> phoneInvalid(@RequestParam (required = false, defaultValue = "1") Integer page,
                                                  @RequestParam (required = false, defaultValue = "10") Integer size,
                                                  @RequestParam (required = false) Long id,
                                                  @RequestParam (required = false) String name) {
        return businessUserInfoService.getPhoneInvalid(page, size, id,name);
    }

    @PutMapping("{id}")
    public boolean normal(@PathVariable Long id) {
        return businessUserInfoService.phoneNormal(id);
    }

    @PostMapping("/adopt/{id}")
    public boolean examineAdopt(@PathVariable Long id){
        return businessUserInfoService.examineAdopt(id);
    }

}
