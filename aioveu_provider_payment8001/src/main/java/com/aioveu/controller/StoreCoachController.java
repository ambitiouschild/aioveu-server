package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.StoreCoach;
import com.aioveu.service.StoreCoachService;
import com.aioveu.vo.StoreCoachUserVO;
import com.aioveu.vo.StoreCoachVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/2/23 0023 16:00
 */
@Slf4j
@RequestMapping("/api/v1/store-coach")
@RestController
public class StoreCoachController {

    @Autowired
    private StoreCoachService storeCoachService;

    @GetMapping("")
    public IPage<StoreCoachVO> list(@RequestParam(required = false, defaultValue = "1") Integer page,
                                    @RequestParam(required = false, defaultValue = "10") Integer size,
                                    @RequestParam(required = false, defaultValue = "1") Integer userType,
                                    @RequestParam Long storeId,
                                    @RequestParam(required = false) boolean hasBindUser) {
        return storeCoachService.getByStoreId(storeId, hasBindUser, userType, page, size);
    }

    @PostMapping("")
    public Boolean create(@RequestBody StoreCoach storeCoach) {
        return storeCoachService.createOrUpdate(storeCoach);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return storeCoachService.deleteCoachById(id);
    }

    @GetMapping("/detail/{id}")
    public StoreCoach get(@PathVariable Long id) {
        return storeCoachService.detail(id);
    }

    @PutMapping("")
    public Boolean update(@RequestBody StoreCoach storeCoach) {
        return storeCoachService.createOrUpdate(storeCoach);
    }

    @GetMapping("/store")
    public List<StoreCoachUserVO> getByStoreId(@RequestParam Integer userType,
                                               @RequestParam(required = false) String storeId,
                                               @RequestParam(required = false) String companyId) {
        return storeCoachService.getCreateUserCoachList(userType,storeId,companyId);
    }

    @GetMapping("/user")
    public List<StoreCoachVO> getStoreCoachUser(@RequestParam Long storeId) {
        return storeCoachService.getStoreCoachUser(storeId);
    }


}
