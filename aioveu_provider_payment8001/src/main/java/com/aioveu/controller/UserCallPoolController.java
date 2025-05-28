package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.UserCallPool;
import com.aioveu.service.UserCallPoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @description
 * @author: xiaoyao
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/user-call-pool")
public class UserCallPoolController {

    @Autowired
    private UserCallPoolService userCallPoolService;

    @GetMapping("")
    public IPage<UserCallPool> list(@RequestParam(required = false, defaultValue = "1") Integer page,
                                    @RequestParam(required = false, defaultValue = "10") Integer size,
                                    @RequestParam String userId) {
        return userCallPoolService.selUserCallPoolByCondition(page, size, userId);
    }

    @PostMapping("")
    public boolean addCount(@RequestParam String userId,
                            @RequestParam Integer count) {
        return userCallPoolService.addCount(userId, count);
    }


    @PutMapping("")
    public void update(@Valid @RequestBody UserCallPool dataDTO) {
        userCallPoolService.modifyUserCallPool(dataDTO);
    }

    @DeleteMapping("")
    public void deleteByIds(@RequestParam Integer id) {
        userCallPoolService.deleteUserCallPoolById(id);
    }


}
