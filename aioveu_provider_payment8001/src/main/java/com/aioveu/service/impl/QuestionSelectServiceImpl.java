package com.aioveu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.QuestionSelectDao;
import com.aioveu.entity.QuestionSelect;
import com.aioveu.exception.SportException;
import com.aioveu.service.QuestionAnswerService;
import com.aioveu.service.QuestionSelectService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class QuestionSelectServiceImpl extends ServiceImpl<QuestionSelectDao, QuestionSelect> implements QuestionSelectService {

    @Autowired
    private QuestionAnswerService questionAnswerService;

    @Override
    public boolean checkSelectLimitNumber(List<Long> selectIds) {
        List<QuestionSelect> questionSelectList = listByIds(selectIds);
        if (CollectionUtils.isEmpty(questionSelectList)) {
            throw new SportException("选项id错误");
        }
        for (QuestionSelect questionSelect : questionSelectList) {
            // 筛选出有人数限制的问题选项
            if (questionSelect.getLimitNumber() != null && questionSelect.getLimitNumber()>0) {
                if (questionAnswerService.getSelectNumberByQuestionId(questionSelect.getEnrollQuestionId(), questionSelect.getId()) >= questionSelect.getLimitNumber()) {
                    throw new SportException(questionSelect.getName() + "报名人数已满");
                }
            }
        }
        return true;
    }
}
