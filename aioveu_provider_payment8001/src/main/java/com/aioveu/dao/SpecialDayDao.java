package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.entity.SpecialDay;
import org.springframework.stereotype.Repository;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface SpecialDayDao extends BaseMapper<SpecialDay> {

    /**
     * 通过日期查找
     * @param date
     * @return
     */
    SpecialDay getByDay(String date);

    /**
     * 通过日期查找
     * @param date
     * @param type
     * @return
     */
    SpecialDay getByDayWithType(String date,Integer type);

    /**
     *
     * @param year
     * @param type
     * @return
     */
    String getDaysByYear(String year, Integer type);

}
