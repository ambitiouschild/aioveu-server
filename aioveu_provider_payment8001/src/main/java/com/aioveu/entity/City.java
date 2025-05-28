package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_city")
@Data
public class City extends IdNameEntity {

    /**
     * 省份Id
     */
    private Long provinceId;

    /**
     * 城市名称
     */
    private String name;

    /**
     * 优先级
     */
    private Integer priority;

}
