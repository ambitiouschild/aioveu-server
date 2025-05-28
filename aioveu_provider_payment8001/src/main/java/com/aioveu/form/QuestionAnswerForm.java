package com.aioveu.form;

import lombok.Data;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/12/8 0008 22:44
 */
@Data
public class QuestionAnswerForm {

    private Long formEnrollId;

    private String userId;

    private List<QuestionAnswerSelectForm> answerSelectList;

    private String shareUserId;

    private Long posterId;

    @Override
    public String toString() {
        return "QuestionAnswerForm{" +
                "formEnrollId=" + formEnrollId +
                ", userId='" + userId + '\'' +
                ", shareUserId='" + shareUserId + '\'' +
                ", posterId=" + posterId +
                '}';
    }
}
