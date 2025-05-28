package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.SpecialDay;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface SpecialDayService extends IService<SpecialDay> {

    /**
     * 通过日期查找
     * @param date
     * @return
     */
    SpecialDay getByDay(String date);

    /**
     * 通过日期查找
     * @param date
     * @return
     */
    SpecialDay getByDayWithType(String date,Integer type);

    /**
     * 从缓存中获取
     * @param date
     * @return
     */
    Boolean isSpecialDayByCache(String date, Integer type);

    /**
     * 是否是放假 包括(节假日和平时周末)
     * @param date
     * @return
     */
    boolean isHoliday(String date);

}
