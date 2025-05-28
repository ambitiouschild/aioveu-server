package com.aioveu.tool;

import lombok.Data;

/**
 * @description 创建班级模板
 * @author: 雒世松
 * @date: 2025/02/06 10:42
 */
@Data
public class GradeTemplateCreateTool {

    /**
     * 班级名称
     */
    private String name;

    /**
     * 课券名称
     */
    private String couponTemplateName;

    /**
     * 消耗课时
     */
    private Integer classHour;

    /**
     * 限制人数
     */
    private Integer limitNumber;

    /**
     * 场馆名称
     */
    private String venueName;

    /**
     * 开始时间
     */
    private String startDayTime;

    /**
     * 结束时间
     */
    private String endDayTime;

    /**
     * 店铺名称
     */
    private String storeName;

}
