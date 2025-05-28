package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.entity.UserInfo;
import com.aioveu.service.UserInfoService;
import com.aioveu.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * @description 公海客户信息控制类
 * @author: 雒世松
 * @date: 2025/9/5 0005 12:01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/user-info")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("upload/{companyId}")
    public String upload(MultipartFile file, @PathVariable Long companyId) {
        return userInfoService.importData(file, companyId, OauthUtils.getCurrentUsername());
    }

    @GetMapping("")
    public IPage<UserInfoVO> list(@RequestParam (required = false) Integer page,
                                  @RequestParam (required = false) Integer size,
                                  @RequestParam Long companyId,
                                  @RequestParam Integer type,
                                  @RequestParam(required = false) String phone) {
        return userInfoService.getList(page, size, companyId, type, OauthUtils.getCurrentUsername(), phone);
    }

    @GetMapping("/{id}")
    public UserInfoDetailVO detail(@PathVariable Long id) {
        return userInfoService.detail(id);
    }

    @GetMapping("/edit/{id}")
    public UserInfoEditVO editDetail(@PathVariable Long id) {
        return userInfoService.edit(id);
    }

    @PutMapping("")
    public boolean update(@RequestBody UserInfo userInfo) {
        return userInfoService.updateById(userInfo);
    }

    @GetMapping("/order")
    public IPage<UserInfoOrderVO> orderList(@RequestParam (required = false) Integer page,
                                            @RequestParam (required = false) Integer size,
                                            @RequestParam String phone) {
        return userInfoService.orderList(page, size, phone);
    }

    @GetMapping("/appointment")
    public IPage<UserInfoOrderVO> appointmentList(@RequestParam (required = false) Integer page,
                                            @RequestParam (required = false) Integer size,
                                            @RequestParam String phone) {
        return userInfoService.appointmentList(page, size, phone);
    }

    @PostMapping("")
    public Boolean create(@Valid @RequestBody UserInfo userInfo) {
        return userInfoService.create(userInfo, OauthUtils.getCurrentUserId(), "添加到公海");
    }

    @GetMapping("/phone/{phone}")
    public UserInfoCreateOrderVO getByPhone(@PathVariable String phone, @RequestParam Long companyId) {
        return userInfoService.getByPhone(phone, companyId);
    }

    @PostMapping("order/{companyId}")
    public boolean uploadOrder(MultipartFile file, @PathVariable Long companyId) {
        userInfoService.importOrder(file, companyId);
        return true;
    }

    @GetMapping("/add")
    public Integer addUserInfoNumber(){
        return userInfoService.addUserInfoNumber();
    }

}
