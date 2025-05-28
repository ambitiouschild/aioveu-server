package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.Audit;
import com.aioveu.entity.City;
import com.aioveu.enums.AuditStatus;
import com.aioveu.enums.AuditType;
import com.aioveu.service.AuditService;
import com.aioveu.service.CityService;
import com.aioveu.vo.IdNameVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/audit")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @GetMapping("/condition")
    public IPage<Audit> listByCondition(@RequestParam(required = false, defaultValue = "1") Integer page,
                                        @RequestParam(required = false, defaultValue = "10") Integer size,
                                        @RequestParam(required = false) String userId,
                                        @RequestParam(required = false) Integer storeId,
                                        @RequestParam(required = false) Integer status,
                                        @RequestParam(required = false) Integer auditType) {
        return auditService.selAuditByCondition(page, size, storeId, userId, status, auditType);
    }

    @PutMapping("/submit")
    public void submit(@RequestBody Audit audit) {
        auditService.submit(audit);
    }

    @PostMapping("/approved")
    public void approved(@RequestBody Audit audit) {
        auditService.approved(audit);
    }

    @PostMapping("/reject")
    public void reject(@RequestBody Audit audit) {
        auditService.reject(audit);
    }

}
