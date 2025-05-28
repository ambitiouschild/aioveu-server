package com.aioveu.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/10 0010 22:02
 */
public class IdUtils {

    public static synchronized  String getStrNumberId(String prefix) {
        Random random = new Random();
        Integer number = random.nextInt(9000) + 1000;
        return prefix + System.currentTimeMillis() + number;
    }

    public static synchronized String getNumberId(){
        Random random = new Random();
        Integer number = random.nextInt(900000) + 100000;
        return System.currentTimeMillis()+ String.valueOf(number);
    }

    public static synchronized String getConsumeCode(){
        return getConsumeCode("");
    }

    public static synchronized String getConsumeCode(String prefix){
        Random random = new Random();
        Integer number = random.nextInt(900000) + 100000;
        String str = System.currentTimeMillis()+"";
        return prefix + str.substring(str.length()-6, str.length()) + number;
    }

    public static String getBlackConsumeCode(String code) {
        if (code == null || code.length()!= 12) {
            return code;
        }
        return code.substring(0, 4) + " " + code.substring(4, 8) + " " + code.substring(8, 12);
    }

    /**
     * 获取四位验证码
     * @return
     */
    public static String get4NumberCode() {
        return Math.round(Math.random()*8999+1000)+"";
    }

    /**
     * 获取六位验证码
     * @return
     */
    public static String get6NumberCode() {
        return Math.round(Math.random()*899999+100000)+"";
    }

    public static String getStringId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static void main(String[] args) throws Exception {
        Date startTime = DateUtils.parseDate("2024-01-12 17:00:00", "yyyy-MM-dd HH:mm:ss");
        Date endTime = DateUtils.parseDate("2024-01-12 18:16:00", "yyyy-MM-dd HH:mm:ss");
        long diffMilliSec = endTime.getTime() - startTime.getTime();
        // 分钟差
        long minutes = diffMilliSec / (60 * 1000);
        int count = (int) (minutes / 15);
        if (minutes % 15 > 0) {
            count += 1;
        }
        System.out.printf("count:" + count);
    }
}
