package com.aioveu.controller;

import com.aioveu.entity.FieldPlanTemplate;
import com.aioveu.form.FieldPlanTemplateForm;
import com.aioveu.service.FieldPlanService;
import com.aioveu.service.FieldPlanTemplateService;
import com.aioveu.utils.SportDateUtils;
import com.aioveu.vo.FieldPlanTemplateVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/field-template")
public class FieldTemplateController {

    @Autowired
    private FieldPlanTemplateService fieldPlanTemplateService;

    @PostMapping("")
    public boolean create(@RequestBody FieldPlanTemplateForm form) {
        return fieldPlanTemplateService.create(form);
    }

    @GetMapping("/list/{storeId}")
    public List<FieldPlanTemplateVO> list(@PathVariable Long storeId, @RequestParam(required = false) String time) {
        return fieldPlanTemplateService.templateList(storeId, time);
    }

    @GetMapping("/{id}")
    public FieldPlanTemplateForm detail(@PathVariable String id) {
        FieldPlanTemplate fieldPlanTemplate = fieldPlanTemplateService.getById(id);
        FieldPlanTemplateForm fieldPlanTemplateForm = new FieldPlanTemplateForm();
        BeanUtils.copyProperties(fieldPlanTemplate, fieldPlanTemplateForm);
        fieldPlanTemplateForm.setStartTime(SportDateUtils.timeFormat(fieldPlanTemplate.getStartTime(), "HH:mm"));
        fieldPlanTemplateForm.setEndTime(SportDateUtils.timeFormat(fieldPlanTemplate.getEndTime(), "HH:mm"));
        List<String> dateList = new ArrayList<>();
        if (!StringUtils.isEmpty(fieldPlanTemplate.getDateList())) {
            dateList = Arrays.stream(fieldPlanTemplate.getDateList().split(",")).collect(Collectors.toList());
        }
        fieldPlanTemplateForm.setDateList(dateList);
        List<Long> fieldIdList = Arrays.stream(fieldPlanTemplate.getFieldIds().split(",")).map(t -> new Long(t)).collect(Collectors.toList());
        fieldPlanTemplateForm.setFieldIdList(fieldIdList);
        return fieldPlanTemplateForm;
    }

    @PutMapping("/status")
    public boolean updateStatus(@RequestParam String id, @RequestParam Integer status) {
        return fieldPlanTemplateService.changeStatus(id, status);
    }

}
