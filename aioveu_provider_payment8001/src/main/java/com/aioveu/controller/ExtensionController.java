package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.dto.ExtensionUserDTO;
import com.aioveu.entity.User;
import com.aioveu.service.ExtensionService;
import com.aioveu.vo.user.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description
 * @author: xiaoyao
 * @date: 2025/10/08
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/extension")
public class ExtensionController {

    @Autowired
    ExtensionService extensionService;


    @GetMapping("")
    public IPage<UserVo> listByCondition(@RequestParam(required = false, defaultValue = "1") Integer page,
                                         @RequestParam(required = false, defaultValue = "10") Integer size,
                                         @RequestParam(required = false) String username,
                                         @RequestParam(required = false) String id) {
        return extensionService.selExtensionUser(page, size, username, id);
    }

    /**
     * 用户通用-修改审核状态为正常状态
     * @param user
     */
    @PostMapping("/modifyExamine")
    public void listByCondition(@RequestBody User user) {
        extensionService.modifyExamine(user);
    }

    @PostMapping("")
    public void create(@RequestBody ExtensionUserDTO dataDTO) {
        extensionService.create(dataDTO);
    }


    @PutMapping("")
    public Integer modifyExtensionUser(@RequestBody User dataDTO) {
        return extensionService.modifyExtensionUser(dataDTO);
    }

    @DeleteMapping("/{id}")
    public Integer deleteBusinessArea(@PathVariable String id) {
        return extensionService.deleteExtensionUser(id);
    }

}
