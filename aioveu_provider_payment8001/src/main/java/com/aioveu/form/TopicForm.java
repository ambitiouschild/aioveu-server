package com.aioveu.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @description 灵活课包
 * @author: 雒世松
 * @date: 2025/8/27 0027 6:20
 */
@Data
public class TopicForm {

    private Long id;

    @NotEmpty(message = "名称不能为空")
    private String name;

    @NotNull(message = "分类id不能为空")
    private Long categoryId;

    @NotEmpty(message = "封面图不能为空")
    private String cover;

    private String color;

    /**
     * 开始报名时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endTime;

    @NotEmpty(message = "介绍不能为空")
    private String introduce;

    @NotEmpty(message = "qa不能为空")
    private String qa;

    @NotNull(message = "样式类型不能为空")
    private Integer styleType;

    @NotNull(message = "主题价格不能为空")
    private BigDecimal price;

    private Integer status;

    @NotNull(message = "用户到店奖励不能为空")
    private BigDecimal reward;

}
