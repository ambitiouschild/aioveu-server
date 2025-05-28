package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.FormEnroll;
import com.aioveu.form.QuestionAnswerForm;
import com.aioveu.service.CodeService;
import com.aioveu.service.FormEnrollService;
import com.aioveu.service.QuestionAnswerService;
import com.aioveu.vo.FormEnrollManagerItemVO;
import com.aioveu.vo.FormEnrollVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/2/23 0023 16:00
 */
@Slf4j
@RequestMapping("/api/v1/formEnroll")
@RestController
public class FormEnrollController {

    private final FormEnrollService formEnrollService;

    private QuestionAnswerService questionAnswerService;

    @Autowired
    private CodeService codeService;

    public FormEnrollController(FormEnrollService formEnrollService, QuestionAnswerService questionAnswerService) {
        this.formEnrollService = formEnrollService;
        this.questionAnswerService = questionAnswerService;
    }

    @GetMapping("/{id}")
    public FormEnrollVO detail(@PathVariable Long id) {
        return formEnrollService.getDetail(id);
    }

    @PutMapping("")
    public boolean update(@Valid @RequestBody FormEnroll formEnroll) {
        return formEnrollService.saveOrUpdate(formEnroll);
    }

    @PostMapping("")
    public boolean create(@Valid @RequestBody FormEnroll formEnroll){
        return formEnrollService.save(formEnroll);
    }

    @PostMapping("/enroll")
    public String createEnroll(@Valid @RequestBody QuestionAnswerForm form){
        return questionAnswerService.enroll(form);
    }

    @GetMapping("")
    public IPage<FormEnrollManagerItemVO> list(@RequestParam(required = false, defaultValue = "1") Integer page,
                                               @RequestParam(required = false, defaultValue = "10") Integer size) {
        return formEnrollService.pageList(page, size);
    }

    @PutMapping("/status")
    public boolean status(@RequestParam Long id, @RequestParam Integer status) {
        return formEnrollService.changeStatus(id, status);
    }

    /**
     * 问卷二维码
     * @param id
     * @return
     */
    @GetMapping("/code/{id}")
    public String pageCode(@PathVariable Long id){
        return codeService.formEnrollPageCode(id);
    }



}
