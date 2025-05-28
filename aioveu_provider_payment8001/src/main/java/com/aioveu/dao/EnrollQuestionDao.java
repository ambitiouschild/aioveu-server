package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.entity.EnrollQuestion;
import com.aioveu.vo.EnrollQuestionVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface EnrollQuestionDao extends BaseMapper<EnrollQuestion> {


    /**
     * 查找
     * @param formEnrollId
     * @return
     */
    List<EnrollQuestionVO> getQuestionList(Long formEnrollId);

}
