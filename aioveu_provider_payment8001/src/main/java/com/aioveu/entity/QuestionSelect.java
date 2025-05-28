package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_question_select")
@Data
public class QuestionSelect extends IdNameEntity {

    private Long enrollQuestionId;

    private String code;

    /**
     * 限制人数
     */
    private Integer limitNumber;

}
