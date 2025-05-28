package com.aioveu.service;

import com.aioveu.tool.GradeEnrollUserNameTimeCancelTool;
import com.aioveu.tool.GradeEnrollUserTimeCancelTool;
import com.aioveu.tool.GradeTemplateCreateTool;

/**
 * @description 课程AI服务类
 * @author: 雒世松
 * @date: 2025/02/12 10:42
 */
public interface GradeAiService {

    /**
     * Ai创建班级模板
     * @param gradeTemplateCreateTool
     * @return
     */
    String aiCreate(GradeTemplateCreateTool gradeTemplateCreateTool);

    /**
     * AI用户根据时间取消约课调用
     * @param gradeEnrollUserTimeCancelTool
     * @return
     */
    String aiUserTimeCancelEnroll(GradeEnrollUserTimeCancelTool gradeEnrollUserTimeCancelTool);

    /**
     * AI用户根据时间和课程名称取消约课调用
     * @param gradeEnrollUserNameTimeCancelTool
     * @return
     */
    String aiUserNameTimeCancelEnroll(GradeEnrollUserNameTimeCancelTool gradeEnrollUserNameTimeCancelTool);

}
