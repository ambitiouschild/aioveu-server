package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_exercise_image")
@Data
public class ExerciseImage extends IdEntity {

    @NotNull(message = "活动不能为空")
    private Long exerciseId;

    private Integer width;

    private Integer height;

    @NotEmpty(message = "地址不能为空")
    private String url;

    /**
     * 图片类型 默认0 封面图 1 详情图
     */
    private Integer imageType;

    private Integer priority;

}
