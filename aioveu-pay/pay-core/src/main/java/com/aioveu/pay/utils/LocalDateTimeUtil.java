package com.aioveu.pay.utils;

import cn.hutool.core.util.StrUtil;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;

public class LocalDateTimeUtil {


    /*
    * 转换
    *
    * */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }


    public static LocalDateTime parseWechatTime(String time) {
        if (StrUtil.isBlank(time)) {
            return null;
        }
        return OffsetDateTime.parse(time)
                .atZoneSameInstant(ZoneId.systemDefault())
                .toLocalDateTime();
    }





}
