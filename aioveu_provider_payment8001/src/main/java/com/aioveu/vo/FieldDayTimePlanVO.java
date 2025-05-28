package com.aioveu.vo;

import lombok.Data;

import java.util.Map;

/**
 * @description 场地 一天计划 的 场地或者教室
 * @author: 雒世松
 * @date: 2025/04/07 19:29
 */
@Data
public class FieldDayTimePlanVO {

    private Long id;

    private String name;

    private Map<String, FieldDayTimePlanItemVO> timeMap;

}
