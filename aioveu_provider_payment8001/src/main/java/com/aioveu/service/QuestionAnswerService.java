package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.QuestionAnswer;
import com.aioveu.form.QuestionAnswerForm;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface QuestionAnswerService extends IService<QuestionAnswer> {

    /**
     * 报名
     * @param questionAnswerForm
     * @return
     */
    String enroll(QuestionAnswerForm questionAnswerForm);


    /**
     * 获取某个问题的某个选项的报名人数
     * @param enrollQuestionId
     * @param selectId
     * @return
     */
    Long getSelectNumberByQuestionId(Long enrollQuestionId, Long selectId);


}
