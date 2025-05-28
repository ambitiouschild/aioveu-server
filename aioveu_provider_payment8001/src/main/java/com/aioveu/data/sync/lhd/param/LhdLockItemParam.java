package com.aioveu.data.sync.lhd.param;

import lombok.Data;


/**
 * @description 锁场item参数类
 * @author: 雒世松
 * @date: 2025/3/31 10:47
 */
@Data
public class LhdLockItemParam {

    /**
     * 场地id
     */
    String fieldId;

    /**
     * 场地名
     */
    String fieldName;

    /**
     * 开始时间
     */
    String beginTime;

    /**
     * 结束时间
     */
    String endTime;

    /**
     * 日期
     */
    String lockDate;

}