package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.dto.CancelGradeMessageDTO;
import com.aioveu.entity.GradeCancelOptions;
import com.aioveu.entity.GradeCancelRecord;
import com.aioveu.enums.DataStatus;
import com.aioveu.form.GradeForm;
import com.aioveu.form.UserGradeListForm;
import com.aioveu.service.GradeCancelOptionsService;
import com.aioveu.service.GradeService;
import com.aioveu.service.GradeSignEvaluateService;
import com.aioveu.vo.*;
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
@RequestMapping("/api/v1/grade")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @Autowired
    private GradeSignEvaluateService gradeSignEvaluateService;

    @Autowired
    private GradeCancelOptionsService gradeCancelOptionsService;


    @PostMapping("cancel-record")
    public IPage<GradeCancelRecordVo> getCancelGradeMessage(@RequestBody CancelGradeMessageDTO dataDTO) {
        return gradeService.getCancelGradeMessageByStoreId(dataDTO);
    }

    @GetMapping("/list/{storeId}")
    public IPage<GradeVO> list(@PathVariable Long storeId,
                              @RequestParam(required = false) String date,
                              @RequestParam(required = false) String status,
                               @RequestParam(required = false) Long coachId,
                               @RequestParam(required = false) String startTime,
                               @RequestParam(required = false) String endTime,
                              @RequestParam(required = false, defaultValue = "1") Integer page,
                              @RequestParam(required = false, defaultValue = "10") Integer size) {
        return gradeService.getByStoreId(storeId, date, status, coachId, startTime, endTime, page, size);
    }

    @GetMapping("/{id}")
    public GradeDetailVO detail(@PathVariable Long id) {
        return gradeService.detail(id);
    }

    @PutMapping("")
    public boolean update(@RequestBody GradeForm form) {
        return gradeService.updateGrade(form);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return gradeService.changeStatus(id, DataStatus.DELETE.getCode());
    }

    @PostMapping("/user-list")
    public List<GradeUserItemVO> userList(@RequestBody UserGradeListForm form) {
        return gradeService.userList(form);
    }

    @GetMapping("/my-available-date")
    public List<GradeUserDateVO> myAvailableDate(@RequestParam Long companyId) {
        return gradeService.myAvailableDate(OauthUtils.getCurrentUserId(), companyId);
    }

    @GetMapping("/available-date/{userId}")
    public List<GradeUserDateVO> getAvailableDateByUserId(@RequestParam Long companyId, @PathVariable String userId) {
        return gradeService.getAvailableDateByUserId(userId, companyId);
    }

    @GetMapping("/detail/{id}")
    public GradeUserDetailVO userDetail(@PathVariable Long id) {
        return gradeService.userDetail(id);
    }

    @GetMapping("/list/coach")
    public IPage<GradeVO> getByCoach(@RequestParam(required = false, defaultValue = "0") Integer type,
                                    @RequestParam(required = false, defaultValue = "1") Integer page,
                                    @RequestParam(required = false, defaultValue = "10") Integer size,
                                     @RequestParam(required = false) String date) {
        return gradeService.getByCoach(OauthUtils.getCurrentUserId(), type, page, size, date);
    }

    @PutMapping("/cancel")
    public boolean cancel(@RequestBody GradeCancelRecord gradeCancelRecord) {
        return gradeService.cancel(gradeCancelRecord, OauthUtils.getCurrentUsername());
    }

    @GetMapping("/time/{id}")
    public GradeTimeRuleVO getTimeType(@PathVariable Long id) {
        return gradeService.getGradeTimeRule(id);
    }

    @PutMapping("/un-sign-back/{id}")
    public boolean unSignBack(@PathVariable Long id) {
        return gradeSignEvaluateService.unSignBack(id, OauthUtils.getCurrentUsername());
    }

    @GetMapping("/get-grade-cancel-options")
    public List<GradeCancelOptions> getGradeCancelOptions(@RequestParam Long companyId) {
        return gradeCancelOptionsService.getGradeCancelOptionsByStatus(companyId, DataStatus.NORMAL.getCode());
    }

    @PostMapping("/save-grade-cancel-options")
    public String saveGradeCancelOptions(@RequestBody GradeCancelOptions gradeCancelOptions) {
        try {
            gradeCancelOptionsService.saveGradeCancelOptions(gradeCancelOptions);
            return null;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @DeleteMapping("/del-grade-cancel-options/{id}")
    public boolean delGradeCancelOptions(@PathVariable Long id) {
        return gradeCancelOptionsService.delGradeCancelOptions(id);
    }

}
