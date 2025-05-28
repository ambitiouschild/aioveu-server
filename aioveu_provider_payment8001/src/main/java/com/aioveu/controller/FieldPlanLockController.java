
package com.aioveu.controller;

import com.aioveu.data.sync.FieldSyncMessage;
import com.aioveu.entity.FieldPlanLock;
import com.aioveu.service.FieldPlanLockService;
import com.aioveu.service.MQMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/field-plan-lock")
public class FieldPlanLockController {

    @Autowired
    private FieldPlanLockService fieldPlanLockService;

    @Autowired
    private MQMessageService mqMessageService;

    @PostMapping("")
    public boolean create(@RequestBody FieldPlanLock form) {
        return fieldPlanLockService.create(form);
    }

    @GetMapping("/list/{storeId}")
    public List<FieldPlanLock> list(@PathVariable Long storeId, @RequestParam(required = false) Long venueId, @RequestParam(required = false) String name) {
        return fieldPlanLockService.getByStoreId(storeId, venueId, name);
    }

    @GetMapping("/{id}")
    public FieldPlanLock detail(@PathVariable Long id) {
        return fieldPlanLockService.getById(id);
    }

    @PutMapping("/status")
    public boolean updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        return fieldPlanLockService.changeStatus(id, status);
    }

    @PostMapping("/sync-platform")
    public boolean sendFieldSyncMessage(@RequestBody FieldSyncMessage fieldSyncMessage) {
        mqMessageService.sendFieldSyncMessage(fieldSyncMessage);
        return true;
    }


}
