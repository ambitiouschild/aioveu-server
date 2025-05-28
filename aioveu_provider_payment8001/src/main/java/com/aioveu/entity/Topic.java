package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_topic")
@Data
public class Topic extends IdNameEntity {

    private Long categoryId;

    private String name;

    private Integer priority;

    private String cover;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private String introduce;

    private String qa;

    private String color;

    private BigDecimal price;

    private Integer styleType;

    private BigDecimal reward;

}
