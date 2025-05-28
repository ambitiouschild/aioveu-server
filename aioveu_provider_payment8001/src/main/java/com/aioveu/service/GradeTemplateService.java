package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.Grade;
import com.aioveu.entity.GradeTemplate;
import com.aioveu.form.GradeTemplateForm;
import com.aioveu.tool.GradeTemplateCreateTool;
import com.aioveu.vo.GradeTemplateDetailVO;
import com.aioveu.vo.GradeTemplateVO;

import java.util.Date;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface GradeTemplateService extends IService<GradeTemplate> {

    /**
     * 创建班级模板
     * @param form
     * @return
     */
    boolean create(GradeTemplateForm form);

    /**
     * 根据id删除课程
     * @param id
     * @return
     */
    boolean deleteById(String id);

    /**
     * 通过课程模板创建新课程
     * @param gradeTemplate
     * @param dayStr
     * @param startTime
     * @param endTime
     * @return
     */
    Grade createByGradeTemplate(GradeTemplate gradeTemplate, String dayStr, String startTime, String endTime);

    /**
     * 创建完整的班级，包括关联表
     * @param gradeTemplate
     * @param dayStr
     * @param startTime
     * @param endTime
     * @return
     */
    Long createFullGrade(GradeTemplate gradeTemplate, String dayStr, String startTime, String endTime);

    /**
     * 获取店铺的班级模板列表
     * @param storeId
     * @return
     */
    IPage<GradeTemplateVO> templateList(Long storeId, String date , Integer page, Integer size);

    /**
     * 获取编辑的详情
     * @param id
     * @return
     */
    GradeTemplateDetailVO detail(String id);

    /**
     * 模板更新
     * @param form
     * @return
     */
    boolean templateUpdate(GradeTemplateForm form);

    /**
     * 修改状态
     * @param id
     * @param status
     * @return
     */
    boolean changeStatus(String id, Integer status);

    /**
     * 获取需要更新的班级模板
     * @return
     */
    List<GradeTemplate> getUpdateTemplate();

    /**
     * 获取时间规则描述
     * @param timeType
     * @param dateList
     * @param startTime
     * @param endTime
     * @return
     */
    String getTimeRule(Integer timeType, String dateList, Date startTime, Date endTime);

}
