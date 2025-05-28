package com.aioveu.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.annotation.TableName;
import com.aioveu.enums.QuestionType;
import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_enroll_question")
@Data
public class EnrollQuestion extends IdNameEntity {

    private Long formEnrollId;

    private Integer priority;

    @JSONField(serialzeFeatures= SerializerFeature.WriteEnumUsingToString)
    private QuestionType questionType;


}
