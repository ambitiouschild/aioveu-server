package com.aioveu.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 雒世松12
 * @description
 * @date 2025/12/9 17:46
 */
@Data
public class FormEnrollVO {

    private Long id;

    private String name;

    private String description;

    private String successMsg;

    private List<EnrollQuestionVO> questionList;

}
