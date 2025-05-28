package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.Company;
import com.aioveu.service.CompanyService;
import com.aioveu.service.CompanyStoreUserService;
import com.aioveu.service.UserExtensionAccountService;
import com.aioveu.vo.UserExtensionAccountVO;
import com.aioveu.vo.api.CompanyItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/2/23 0023 16:00
 */
@Slf4j
@RequestMapping("/api/v1/user-extension-account")
@RestController
public class UserExtensionAccountController {

    @Autowired
    private UserExtensionAccountService userExtensionAccountService;

    @GetMapping("")
    public IPage<UserExtensionAccountVO> list(@RequestParam(required = false, defaultValue = "1") Integer page,
                                              @RequestParam(required = false, defaultValue = "10") Integer size,
                                              @RequestParam(required = false) String keyword) {
        return userExtensionAccountService.getAll(page, size, keyword);
    }





}
