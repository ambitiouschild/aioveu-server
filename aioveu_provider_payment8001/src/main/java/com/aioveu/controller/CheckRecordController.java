package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.entity.CheckRecord;
import com.aioveu.service.CheckRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/9/5 0005 12:01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/check-record")
public class CheckRecordController {

    @Autowired
    private CheckRecordService checkRecordService;

    @PostMapping("")
    public boolean create(@RequestBody CheckRecord checkRecord){
        return checkRecordService.create(checkRecord, OauthUtils.getCurrentUsername());
    }

    @GetMapping("")
    public IPage<CheckRecord> list(@RequestParam(required = false, defaultValue = "1") Integer page,
                                   @RequestParam(required = false, defaultValue = "10") Integer size,
                                   @RequestParam Long storeId) {
        return checkRecordService.getList(page, size, OauthUtils.getCurrentUsername(), storeId);
    }

    @PutMapping("/{id}")
    public String check(@PathVariable Long id) {
        return checkRecordService.check(id, OauthUtils.getCurrentUsername());
    }

    @DeleteMapping("")
    public boolean batchDelete(@RequestBody List<Long> ids){
        return checkRecordService.batchDelete(ids, OauthUtils.getCurrentUsername());
    }

    @PostMapping("/check/{prefix}")
    public String uploadPreCheck(@PathVariable String prefix, MultipartFile file
            , @RequestParam Long storeId
            , @RequestParam Long companyId
            ,@RequestParam(required = false) String scanText) throws Exception {
        return checkRecordService.uploadPreCheck(storeId, companyId, prefix, file, scanText);
    }


}
