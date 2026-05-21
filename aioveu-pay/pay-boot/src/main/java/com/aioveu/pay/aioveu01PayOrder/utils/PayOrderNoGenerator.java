package com.aioveu.pay.aioveu01PayOrder.utils;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @ClassName: PayOrderNoGenerator
 * @Description TODO 生产级推荐版（✅ 强烈建议） Redis 序列号（支付系统标配）
 *                     ✅ 永不重复
 *                     ✅ 天然有序
 *                     ✅ 对账极爽
 *                     ❌ 上线前一定要换成 Redis 版本
 *                     ✅ paymentNo 一旦重复，对账会炸
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 17:46
 * @Version 1.0
 **/

// 单号生成器
@Component
@RequiredArgsConstructor
public class PayOrderNoGenerator {

    private final StringRedisTemplate redisTemplate;

    private static final String PAY_PREFIX = "PAY";
    private static final String REFUND_PREFIX = "RF";
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyyMMdd");

    public String generatePaymentNo() {
        return generate(PAY_PREFIX);
    }

    public String generateRefundNo() {
        return generate(REFUND_PREFIX);
    }

    private String generate(String prefix) {
        String date = LocalDateTime.now().format(FMT);
        Long seq = redisTemplate.opsForValue()
                .increment("pay:no:" + prefix + ":" + date);
        return prefix + date + String.format("%06d", seq);
    }
}