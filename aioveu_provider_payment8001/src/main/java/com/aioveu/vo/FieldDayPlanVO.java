package com.aioveu.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description 场地 一天计划
 * @author: 雒世松
 * @date: 2025/04/07 19:29
 */
@Data
public class FieldDayPlanVO {

    private String week;

    private String dateStr;

    private Date date;

    /**
     * 时间渲染
     */
    private List<HourVO> hourList;

    /**
     * 场地渲染
     */
    private List<FieldDayTimePlanVO> fieldList;

}
