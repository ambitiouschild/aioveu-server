package com.aioveu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.SpecialDayDao;
import com.aioveu.entity.SpecialDay;
import com.aioveu.service.SpecialDayService;
import com.aioveu.utils.RedisUtil;
import com.aioveu.utils.SportDateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class SpecialDayServiceImpl extends ServiceImpl<SpecialDayDao, SpecialDay> implements SpecialDayService {

    @Autowired
    private RedisUtil redisUtil;

    private static final String SPECIAL_DAY_KEY = "special_day_key:";

    @Override
    public SpecialDay getByDay(String date) {
        return getBaseMapper().getByDay(date);
    }

    @Override
    public SpecialDay getByDayWithType(String date, Integer type) {
        return getBaseMapper().getByDayWithType(date,type);
    }

    @Override
    public Boolean isSpecialDayByCache(String date, Integer type) {
        String key = SPECIAL_DAY_KEY + date.substring(0, 4);
        Object value = redisUtil.hGet(key, type.toString());
        if (value == null) {
            value = getBaseMapper().getDaysByYear(date.substring(0,4), type);
            redisUtil.hSet(key,type.toString(), value);
        }
        return value == null ? false : value.toString().contains(date);
    }

    @Override
    public boolean isHoliday(String date) {
        SpecialDay specialDay = getByDay(date);
        if (specialDay != null) {
            if (specialDay.getSpecialType() == 1) {
                // 法定节假日
                return true;
            } else if (specialDay.getSpecialType() == 0) {
                // 调休日 需要上班
                return false;
            }
        } else {
            try {
                // 普通周末 放假
                return SportDateUtils.isWeekDay(DateUtils.parseDate(date, "yyyy-MM-dd"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
