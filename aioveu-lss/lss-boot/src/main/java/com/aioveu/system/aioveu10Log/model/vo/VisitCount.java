package com.aioveu.system.aioveu10Log.model.vo;

import lombok.Data;

/**
 * @ClassName: VisitCount
 * @Description TODO  特定日期访问统计
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 18:10
 * @Version 1.0
 **/

@Data
public class VisitCount {

    /**
     * 日期 yyyy-MM-dd
     */
    private String date;

    /**
     * 访问次数
     */
    private Integer count;
}
