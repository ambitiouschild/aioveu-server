package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_region")
@Data
public class Region extends IdNameEntity {

    /**
     * 城市Id
     */
    private Long cityId;

    /**
     * 区名称
     */
    private String name;

    /**
     * 优先级
     */
    private Integer priority;

}
