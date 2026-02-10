package com.aioveu.pay.aioveu01PayOrder.utils;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName: OrderNoGenerator
 * @Description TODO 单号生成器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 17:46
 * @Version 1.0
 **/

// 单号生成器
@Component
public class OrderNoGenerator {

    private static final String PAY_PREFIX = "PAY";
    private static final String REFUND_PREFIX = "RF";

    public static String generatePaymentNo() {
        return generateNo(PAY_PREFIX);
    }

    public static String generateRefundNo() {
        return generateNo(REFUND_PREFIX);
    }

    private static String generateNo(String prefix) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timeStr = sdf.format(new Date());
        String randomStr = RandomStringUtils.randomNumeric(6);
        return prefix + timeStr + randomStr;
    }
}
