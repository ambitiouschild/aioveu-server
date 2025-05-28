package com.aioveu.controller;

import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.form.ExerciseCountTemplateForm;
import com.aioveu.service.ExerciseCountTemplateService;
import com.aioveu.vo.ExerciseCountPayVO;
import com.aioveu.vo.ExerciseCountTemplateVO;
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
@RequestMapping("/api/v1/exercise-count-template")
public class ExerciseCountTemplateController {

    @Autowired
    private ExerciseCountTemplateService exerciseCountTemplateService;

    @PostMapping("")
    public String create(@RequestBody ExerciseCountTemplateForm form) {
        return exerciseCountTemplateService.createTemplate(form, OauthUtils.getCurrentUsername());
    }

    @GetMapping("/list/{appId}")
    public List<ExerciseCountTemplateVO> list(@PathVariable String appId) {
        return exerciseCountTemplateService.getUserTemplate(OauthUtils.getCurrentUsername(), appId);
    }

    @GetMapping("/pay-info/{exerciseId}")
    public ExerciseCountPayVO payInfo(@PathVariable Long exerciseId) {
        return exerciseCountTemplateService.getPayInfo(exerciseId);
    }

    @GetMapping("/{id}")
    public ExerciseCountTemplateForm detail(@PathVariable String id) {
        return exerciseCountTemplateService.detail(id);
    }
//
//    @PutMapping("")
//    public boolean update(@RequestBody GradeTemplateForm form) {
//        return gradeTemplateService.templateUpdate(form);
//    }
//
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable String id) {
        return exerciseCountTemplateService.deleteById(id);
    }
//
//    @PutMapping("/status")
//    public boolean updateStatus(@RequestParam String id, @RequestParam Integer status) {
//        return gradeTemplateService.changeStatus(id, status);
//    }

    



}
