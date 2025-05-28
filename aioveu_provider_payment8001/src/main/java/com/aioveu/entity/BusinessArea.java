package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_business_area")
@Data
public class BusinessArea extends IdNameEntity {

    /**
     * 区域Id
     */
    private Long regionId;

    /**
     * 商圈名称
     */
    private String name;

    /**
     * 优先级
     */
    private Integer priority;

}
