package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.Permission;
import com.aioveu.service.MenuPermissionService;
import com.aioveu.service.PermissionService;
import com.aioveu.vo.MenuPermissionVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @description
 * @author: 雒世松
 * @date: 2024/11/08 0031 21:25
 */
@Slf4j
@RequestMapping("/api/v1/permission")
@RestController
public class PermissionRestController {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private MenuPermissionService menuPermissionService;

    @GetMapping("/list")
    public IPage<Permission> webList(@RequestParam(required = false, defaultValue = "1") Integer page,
                               @RequestParam(required = false, defaultValue = "10") Integer size,
                               @RequestParam(required = false) String keyword,
                               @RequestParam(required = false) Boolean needToken) {
        return permissionService.getList(page, size, keyword, needToken);
    }

    @GetMapping("/menu-list")
    public IPage<MenuPermissionVo> getByMenuCode(@RequestParam(required = false, defaultValue = "1") Integer page,
                                                 @RequestParam(required = false, defaultValue = "10") Integer size,
                                                 @RequestParam String menuCode) {
        return menuPermissionService.getByMenuCode(page, size, menuCode);
    }

    @PostMapping("")
    public Long add(@Valid  @RequestBody Permission permission) {
        return permissionService.create(permission);
    }

    @GetMapping("/{id}")
    public Permission getDetail(@PathVariable Long id) {
        return permissionService.getById(id);
    }

    @PutMapping("")
    public boolean update(@Valid @RequestBody Permission permission) {
        return permissionService.updatePermission(permission);
    }

    @DeleteMapping("")
    public boolean deleteById(@RequestParam Long id) {
        return permissionService.deleteById(id);
    }

}
