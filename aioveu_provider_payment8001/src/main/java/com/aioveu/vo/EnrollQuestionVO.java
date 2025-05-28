package com.aioveu.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 雒世松12
 * @description
 * @date 2025/12/9 17:46
 */
@Data
public class EnrollQuestionVO {

    private Long id;

    private String name;

    private Integer questionType;

    private List<QuestionSelectVO> selectList;

}
