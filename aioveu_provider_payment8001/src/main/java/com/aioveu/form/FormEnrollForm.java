package com.aioveu.form;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/12/8 0008 22:44
 */
@Data
public class FormEnrollForm {

    private String name;

    private String description;

    private Date startTime;

    private Date endTime;

    private String successMsg;

    private List<QuestionForm> questionFormList;

}
