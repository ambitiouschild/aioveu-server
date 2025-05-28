package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.QuestionSelect;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface QuestionSelectService extends IService<QuestionSelect> {


    /**
     * 检查表单选项人数限制
     * @param selectIds
     * @return
     */
    boolean checkSelectLimitNumber(List<Long> selectIds);


}
