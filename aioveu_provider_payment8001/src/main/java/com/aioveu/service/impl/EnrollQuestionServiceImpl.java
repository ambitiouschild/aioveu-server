package com.aioveu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.EnrollQuestionDao;
import com.aioveu.entity.EnrollQuestion;
import com.aioveu.entity.QuestionSelect;
import com.aioveu.enums.QuestionType;
import com.aioveu.exception.SportException;
import com.aioveu.form.QuestionAnswerSelectForm;
import com.aioveu.form.QuestionForm;
import com.aioveu.service.EnrollQuestionService;
import com.aioveu.service.QuestionSelectService;
import com.aioveu.vo.EnrollQuestionVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class EnrollQuestionServiceImpl extends ServiceImpl<EnrollQuestionDao, EnrollQuestion> implements EnrollQuestionService {

    @Autowired
    private QuestionSelectService questionSelectService;

    @Autowired
    private EnrollQuestionDao enrollQuestionDao;


    @Override
    public boolean batchCreate(List<QuestionForm> questionFormList, Long formEnrollId) {
        if (CollectionUtils.isEmpty(questionFormList)) {
            throw new SportException("问题不能为空");
        }
        for (QuestionForm questionForm : questionFormList) {
            EnrollQuestion enrollQuestion = new EnrollQuestion();
            enrollQuestion.setFormEnrollId(formEnrollId);
            enrollQuestion.setQuestionType(QuestionType.of(questionForm.getQuestionType()));
            enrollQuestion.setName(questionForm.getName());
            save(enrollQuestion);

            if (enrollQuestion.getQuestionType() != QuestionType.INPUT) {
                String questionSelect = questionForm.getQuestionSelect();
                if (StringUtils.isEmpty(questionSelect)) {
                    throw new SportException(questionForm.getName() + "的选项不能为空");
                }
                String[] selectArray = questionSelect.split(",");

                List<QuestionSelect> questionSelectList = new ArrayList<>();
                for (int i=0; i<selectArray.length; i++) {
                    QuestionSelect item = new QuestionSelect();
                    item.setEnrollQuestionId(enrollQuestion.getId());
                    item.setName(selectArray[i]);
                    questionSelectList.add(item);
                }
                questionSelectService.saveBatch(questionSelectList);
            }
        }
        return true;
    }

    @Override
    public boolean checkSelectLimitNumber(List<QuestionAnswerSelectForm> answerSelectList) {
        List<EnrollQuestion> enrollQuestionList = listByIds(answerSelectList.stream().map(QuestionAnswerSelectForm::getEnrollQuestionId).collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(enrollQuestionList)) {
            throw new SportException("报名选项错误");
        }
        Map<Long, EnrollQuestion> enrollQuestionMap = enrollQuestionList.stream().filter(
                item -> item.getQuestionType().equals(QuestionType.SELECT) ||
                item.getStatus() == 1 ||
                item.getQuestionType().equals(QuestionType.MULTI_SELECT)).collect(Collectors.toMap(EnrollQuestion::getId, Function.identity()));
        if (enrollQuestionMap.size() == 0) {
            return true;
        }
        List<Long> selectIds = answerSelectList.stream()
                .filter(item -> enrollQuestionMap.get(item.getEnrollQuestionId())!=null && item.getSelectIds()!=null)
                .flatMapToLong(item -> {
                    String[] idArrays = item.getSelectIds().split(",");
                    return Arrays.asList(idArrays).stream().mapToLong(Long::new);
                })
                .boxed()
                .collect(Collectors.toList());

        return questionSelectService.checkSelectLimitNumber(selectIds);
    }

    @Override
    public List<EnrollQuestionVO> getQuestionList(Long formEnrollId) {
        return enrollQuestionDao.getQuestionList(formEnrollId);
    }
}
