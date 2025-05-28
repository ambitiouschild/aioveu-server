package com.aioveu.controller;

import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.form.GradeEnrollUserForm;
import com.aioveu.form.HelpCancelGradeEnrollUserForm;
import com.aioveu.form.HelpGradeEnrollUserForm;
import com.aioveu.service.GradeEnrollUserService;
import com.aioveu.service.GradeFixedUserService;
import com.aioveu.vo.GradeEnrollUserDetailVO;
import com.aioveu.vo.GradeEnrollUserItemVO;
import com.aioveu.vo.GradeEnrollUserSimpleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/9/5 0005 12:01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/grade-enroll-user")
public class GradeEnrollUserController {

    @Autowired
    private GradeEnrollUserService gradeEnrollUserService;

    @Autowired
    private GradeFixedUserService gradeFixedUserService;

    @PostMapping("")
    public boolean create(@Valid @RequestBody GradeEnrollUserForm form) {
        return gradeEnrollUserService.create(form);
    }

    @GetMapping("/list/{userId}")
    public List<GradeEnrollUserItemVO> list(@PathVariable String userId,
                                            @RequestParam(required = false) Integer status,
                                            @RequestParam Long companyId,
                                            @RequestParam(required = false) Long storeId) {
        return gradeEnrollUserService.list(userId, status, companyId, storeId);
    }

    @GetMapping("/{id}")
    public GradeEnrollUserDetailVO detail(@PathVariable Long id) {
        return gradeEnrollUserService.detail(id);
    }

    @DeleteMapping("/{id}")
    public boolean cancel(@PathVariable Long id) {
        return gradeEnrollUserService.cancel(id, OauthUtils.getCurrentUserId());
    }

    @DeleteMapping("/fixed/{id}")
    public boolean cancelFixed(@PathVariable Long id) {
        return gradeFixedUserService.cancelFixed(id, OauthUtils.getCurrentUserId());
    }

    @DeleteMapping("/all/{id}")
    public boolean cancelAll(@PathVariable Long id) {
        return gradeEnrollUserService.cancelAll(id);
    }

    @GetMapping("/sign/{gradeId}")
    public List<GradeEnrollUserSimpleVO> enrollUserList(@PathVariable Long gradeId) {
        return gradeEnrollUserService.getByGradeId(gradeId, OauthUtils.getCurrentUserId());
    }

    @PutMapping("/sign/{id}")
    public boolean sign(@PathVariable Long id) {
        return gradeEnrollUserService.sign(id, OauthUtils.getCurrentUserId());
    }

    @GetMapping("/evaluate/{gradeId}")
    public List<GradeEnrollUserSimpleVO> evaluateList(@PathVariable Long gradeId) {
        return gradeEnrollUserService.getEvaluateListByGradeId(gradeId);
    }

    @PutMapping("/evaluate/{id}")
    public boolean evaluate(@PathVariable Long id, @RequestParam String message) {
        return gradeEnrollUserService.evaluate(id, message, OauthUtils.getCurrentUserId());
    }

    @PostMapping("/help")
    public boolean helpEnroll(@Valid @RequestBody HelpGradeEnrollUserForm form) {
        return gradeEnrollUserService.helpEnroll(form);
    }

    @PostMapping("/help-cancel")
    public boolean helpCancelEnroll(@Valid @RequestBody HelpCancelGradeEnrollUserForm form) {
        return gradeEnrollUserService.helpCancelEnroll(form);
    }

    @PutMapping("/edit-name/{id}")
    public boolean editChildName(@PathVariable Long id, @RequestParam String childName) {
        return gradeEnrollUserService.editChildName(id, childName);
    }
}
