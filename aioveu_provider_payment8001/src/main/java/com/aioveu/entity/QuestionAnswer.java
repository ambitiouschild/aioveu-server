package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_question_answer")
@Data
public class QuestionAnswer extends IdEntity {

    private Long formEnrollId;

    private Long enrollQuestionId;

    private String userId;

    private String selectIds;

    private String answer;

}
