package com.aioveu.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @description 地推
 * @author: 雒世松
 * @date: 2025/8/27 0027 6:20
 */
@Data
public class PushExerciseForm  {

    private Long id;

    private Long categoryId;

    @NotNull(message = "主题id不能为空")
    private Long topicId;

    @NotEmpty(message = "名称不能为空")
    private String name;

    @NotEmpty(message = "简介不能为空")
    private String description;

    private Integer limitNumber;

    /**
     * 活动开始报名时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 活动结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private Long storeId;

    @NotNull(message = "价格不能为空")
    private BigDecimal price;

    private BigDecimal originalPrice;

    private List<String> imageList;

}
