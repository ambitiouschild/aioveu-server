package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.EnrollQuestion;
import com.aioveu.form.QuestionAnswerSelectForm;
import com.aioveu.form.QuestionForm;
import com.aioveu.vo.EnrollQuestionVO;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface EnrollQuestionService extends IService<EnrollQuestion> {

    /**
     * 批量创建
     * @param questionFormList
     * @param formEnrollId
     * @return
     */
    boolean batchCreate(List<QuestionForm> questionFormList, Long formEnrollId);

    /**
     * 根据表单报名id查找问题列表
     * @param formEnrollId
     * @return
     */
    List<EnrollQuestionVO> getQuestionList(Long formEnrollId);

    /**
     * 判断选项报名人数限制
     * @param answerSelectList
     * @return
     */
    boolean checkSelectLimitNumber(List<QuestionAnswerSelectForm> answerSelectList);


}
