package com.aioveu.data.sync.lhd.param;

import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/3/31 10:47
 */
@Data
public class LhdFieldPlan {

    /**
     * 场馆名
     */
    private String stadiumItemName;

    /**
     * 场馆id
     */
    private String stadiumItemId;

    /**
     * 场地名
     */
    private String fieldName;

    /**
     * 场地id
     */
    private String fieldId;

    /**
     * 订场时间
     */
    private String resourceDate;

    private String beginTime;

    private String endTime;

    /**
     * 地状态
     */
    private Integer paymentStatus;

}