package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.service.UserInfoPublicService;
import com.aioveu.vo.UserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/9/5 0005 12:01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/user-info-public")
public class UserInfoPublicController {

    @Autowired
    private UserInfoPublicService userInfoPublicService;

    @PostMapping("upload")
    public Boolean upload(MultipartFile file) {
        return userInfoPublicService.importData(file);
    }

    @GetMapping("")
    public IPage<UserInfoVO> list(@RequestParam (required = false) Integer page,
                                  @RequestParam (required = false) Integer size) {
        return userInfoPublicService.getList(page, size);
    }
}
