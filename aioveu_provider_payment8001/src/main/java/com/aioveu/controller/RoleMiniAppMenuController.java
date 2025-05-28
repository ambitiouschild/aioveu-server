package com.aioveu.controller;

import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.entity.RoleMiniAppMenu;
import com.aioveu.service.RoleMiniAppMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/2/23 0023 16:00
 */
@Slf4j
@RequestMapping("/api/v1/role-mini-app-menu")
@RestController
public class RoleMiniAppMenuController {

    @Autowired
    private RoleMiniAppMenuService roleMiniAppMenuService;

    @GetMapping("/role")
    public List<RoleMiniAppMenu> getByRole(@RequestParam Long storeId) {
        return roleMiniAppMenuService.getByUserRole(storeId);
    }



}
