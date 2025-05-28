package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.FieldPlan;
import com.aioveu.entity.FieldPlanTemplate;
import com.aioveu.form.FieldPlanTemplateForm;
import com.aioveu.vo.FieldPlanTemplateVO;

import java.sql.Time;
import java.util.List;

public interface FieldPlanTemplateService extends IService<FieldPlanTemplate> {

    List<FieldPlanTemplateVO> templateList(Long storeId, String dateStr);

    boolean changeStatus(String id, Integer status);

    /**
     * 获取有效的场地计划模板
     * @return 
     */
    List<FieldPlanTemplate> getUpdateTemplate();

    boolean create(FieldPlanTemplateForm form);

    /**
     * 根据订场模版id创建订场计划
     * @param id
     * @return
     */
    List<Long> createFullFieldPlanById(String id);

    List<Long> createFullFieldPlan(FieldPlanTemplate fieldPlanTemplate, String dayStr, String startTime, String endTime);

    List<FieldPlan> createByFieldPlanTemplate(FieldPlanTemplate fieldPlanTemplate, String dayStr, Time startTime, Time endTime);
}
