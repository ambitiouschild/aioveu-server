package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.dto.FieldPlanDTO;
import com.aioveu.entity.FieldPlan;
import com.aioveu.service.FieldPlanService;
import com.aioveu.vo.FieldPlanVO;
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
@RequestMapping("/api/v1/field-plan")
@RestController
public class FieldPlanController {

    @Autowired
    private FieldPlanService fieldPlanService;

    @PostMapping("/getByUser/{userId}")
    public List<FieldPlanDTO> getByUser(@PathVariable String userId, @RequestParam Long companyId, @RequestParam Integer status) {
        return fieldPlanService.getByUser(userId, companyId, status);
    }

    @GetMapping("")
    public IPage<FieldPlanVO> listByCondition(@RequestParam(required = false, defaultValue = "1") Integer page,
                                              @RequestParam(required = false, defaultValue = "10") Integer size,
                                              @RequestParam(required = false) Long storeId,
                                              @RequestParam(required = false) Long venueId,
                                              @RequestParam(required = false) Long fieldId,
                                              @RequestParam(required = false) String date,
                                              @RequestParam(required = false) Integer status) {
        return fieldPlanService.listByCondition(page, size, storeId, venueId, fieldId, date, status);
    }

    @GetMapping("/{id}")
    public FieldPlan detail(@PathVariable Long id) {
        return this.fieldPlanService.getById(id);
    }

    @PostMapping("")
    public void create(@RequestBody FieldPlanVO form) {
        fieldPlanService.saveFieldPlan(form);
    }

    @PutMapping("/status")
    public void updateStatus(@RequestParam List<Long> ids, @RequestParam String remark, @RequestParam Integer status) {
        fieldPlanService.changeStatus(ids, remark, status);
    }

}
