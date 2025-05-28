package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.form.GradeTemplateForm;
import com.aioveu.service.GradeTemplateService;
import com.aioveu.vo.GradeTemplateDetailVO;
import com.aioveu.vo.GradeTemplateVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/9/5 0005 12:01
 */

@Slf4j
@RestController
@RequestMapping("/api/v1/grade-template")
public class GradeTemplateController {

    @Autowired
    private GradeTemplateService gradeTemplateService;

    @PostMapping("")
    public boolean create(@Valid @RequestBody GradeTemplateForm form) {
        return gradeTemplateService.create(form);
    }

    @GetMapping("/list/{storeId}")
    public IPage<GradeTemplateVO> list(@PathVariable Long storeId,
                                       @RequestParam(required = false) String date,
                                       @RequestParam(required = false, defaultValue = "1") Integer page,
                                       @RequestParam(required = false, defaultValue = "10") Integer size) {
        return gradeTemplateService.templateList(storeId,date,page,size);
    }

    @GetMapping("/{id}")
    public GradeTemplateDetailVO detail(@PathVariable String id) {
        return gradeTemplateService.detail(id);
    }

    @PutMapping("")
    public boolean update(@RequestBody GradeTemplateForm form) {
        return gradeTemplateService.templateUpdate(form);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable String id) {
        return gradeTemplateService.deleteById(id);
    }

    @PutMapping("/status")
    public boolean updateStatus(@RequestParam String id, @RequestParam Integer status) {
        return gradeTemplateService.changeStatus(id, status);
    }

    



}
