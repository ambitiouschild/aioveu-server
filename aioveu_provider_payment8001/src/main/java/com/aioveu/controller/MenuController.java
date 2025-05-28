package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.Menu;
import com.aioveu.entity.MenuPermission;
import com.aioveu.service.MenuPermissionService;
import com.aioveu.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/9/5 0005 12:01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuPermissionService menuPermissionService;

    @GetMapping("/list")
    public IPage<Menu> webList(@RequestParam(required = false, defaultValue = "1") Integer page,
                               @RequestParam(required = false, defaultValue = "10") Integer size,
                               @RequestParam(required = false) String parentCode,
                               @RequestParam(required = false) String keyword,
                               @RequestParam(required = false) Integer type) {
        return menuService.getWebList(page, size, type, parentCode, keyword);
    }
    @GetMapping("/{id}")
    public Menu getDetail(@PathVariable Long id) {
        return menuService.getById(id);
    }

    @PostMapping("")
    public boolean save(@Valid @RequestBody Menu menu) {
        return menuService.saveOrUpdateMenu(menu);
    }

    @PostMapping("/permission")
    public boolean addMenuPermission(@Valid @RequestBody List<MenuPermission> menuPermissions) {
        return menuPermissionService.addBatch(menuPermissions);
    }

    @DeleteMapping("/permission")
    public boolean deleteMenuPermission(@RequestParam Long id) {
        return menuPermissionService.deleteById(id);
    }

    @PutMapping("")
    public boolean update(@Valid @RequestBody Menu menu) {
        return menuService.saveOrUpdateMenu(menu);
    }

    @DeleteMapping("")
    public boolean deleteByCode(@RequestParam String code) {
        return menuService.deleteById(code);
    }

}
