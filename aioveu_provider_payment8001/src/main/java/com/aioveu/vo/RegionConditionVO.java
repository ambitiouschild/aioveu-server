package com.aioveu.vo;

import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/12/3 13:16
 */
@Data
public class RegionConditionVO {

    /**
     * 省份Id
     */
    private Long provinceId;
    /**
     * 城市Id
     */
    private Long cityId;
    /**
     * 省份名称
     */
    private String provinceName;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 区域Id
     */
    private Long id;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 优先级
     */
    private int priority;

}
