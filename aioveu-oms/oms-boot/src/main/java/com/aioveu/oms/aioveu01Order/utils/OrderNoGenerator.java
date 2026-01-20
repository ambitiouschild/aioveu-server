package com.aioveu.oms.aioveu01Order.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: OrderNoGenerator
 * @Description TODO  订单号生成器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/20 18:06
 * @Version 1.0
 **/

// 订单号生成器
@Component
public class OrderNoGenerator {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final AtomicInteger SEQUENCE = new AtomicInteger(1);
    private static final int MAX_SEQUENCE = 9999;

    /**
     * 生成订单号
     * 格式: 日期(8位) + 用户ID后6位 + 4位序列号
     */
    public static String generateOrderNo(Long memberId) {
        String date = LocalDate.now().format(FORMATTER);
        String memberStr = String.format("%06d", memberId % 1000000);
        int seq = SEQUENCE.getAndUpdate(i -> i >= MAX_SEQUENCE ? 1 : i + 1);
        String sequenceStr = String.format("%04d", seq);

        return date + memberStr + sequenceStr;
    }
}
