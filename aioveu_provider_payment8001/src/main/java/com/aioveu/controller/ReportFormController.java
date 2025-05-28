package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.entity.ReportForm;
import com.aioveu.service.ReportFormService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/9/5 0005 12:01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/report-form")
public class ReportFormController {

    @Autowired
    private ReportFormService reportFormService;

    @PostMapping("")
    public boolean create(@RequestBody ReportForm reportForm){
        return reportFormService.create(reportForm, OauthUtils.getCurrentUsername());
    }

    @GetMapping("")
    public IPage<ReportForm> list(@RequestParam(required = false, defaultValue = "1") Integer page,
                                   @RequestParam(required = false, defaultValue = "10") Integer size,
                                   @RequestParam Long storeId) {
        return reportFormService.getList(page, size, storeId, OauthUtils.getCurrentUsername());
    }

    @PutMapping("/read")
    public boolean check(@RequestBody List<Long> ids) {
        return reportFormService.read(ids);
    }

    @DeleteMapping("")
    public boolean batchDelete(@RequestBody List<Long> ids){
        return reportFormService.batchDelete(ids, OauthUtils.getCurrentUsername());
    }

}
