package com.aioveu.oms.aioveu01Order.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import static com.mysql.cj.util.TimeUtil.DATE_FORMATTER;

/**
 * @ClassName: OrderNoGenerator
 * @Description TODO  订单号生成器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/20 18:06
 * @Version 1.0
 **/

// 订单号生成器   因为当前实现是顺序递增的，在多实例部署时会有重复风险。
@Component
public class OrderNoGenerator {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final AtomicInteger SEQUENCE = new AtomicInteger(1);
    private static final int MAX_SEQUENCE = 9999;

    private static final Random RANDOM = new Random();


    //方案2：使用 Redis 分布式序列（高并发场景推荐）
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final String ORDER_SEQUENCE_KEY = "order:sequence:";

    /**
     * 生成订单号
     * 格式: 日期(8位) + 用户ID后6位 + 4位序列号
     * 因为当前实现是顺序递增的，在多实例部署时会有重复风险。
     */
    public static String generateOrderNo(Long memberId) {
        String date = LocalDate.now().format(FORMATTER);
        String memberStr = String.format("%06d", memberId % 1000000);
        int seq = SEQUENCE.getAndUpdate(i -> i >= MAX_SEQUENCE ? 1 : i + 1);
        String sequenceStr = String.format("%04d", seq);

        return date + memberStr + sequenceStr;
    }

    /**
     * 生成订单号（方案1：时间戳+随机数）
     * 格式: 日期时间(8位) + 用户ID后6位 + 4位随机数
     */
    public static String generateOrderNoRandom(Long memberId) {
        // 1. 时间戳部分（14位）
        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
        //日期(8位)
        String date = LocalDate.now().format(FORMATTER);

        // 2. 用户ID部分（6位）
        String memberPart = String.format("%06d", (memberId == null ? 0 : memberId) % 1000000);

        // 3. 随机数部分（4位）
        String randomPart = String.format("%04d", ThreadLocalRandom.current().nextInt(10000));

        return date + memberPart + randomPart;
    }

    /**
     * 生成订单号（方案2：雪花算法风格）
     * 格式: 时间戳(41位压缩到10位) + 工作机器ID(3位) + 序列号(4位) + 用户ID(4位) + 随机数(3位)
     */
    public static String generateSnowflakeStyleOrderNo(Long memberId, Integer workerId) {
        // 1. 时间戳（10位，毫秒级）
        long timestamp = System.currentTimeMillis();
        // 转换为10位字符串（从2024-01-01开始计算）
        long startTimestamp = 1704067200000L; // 2024-01-01 00:00:00
        long timeDiff = timestamp - startTimestamp;
        String timePart = String.format("%010d", timeDiff);

        // 2. 工作机器ID（3位，默认随机生成）
        int worker = (workerId != null ? workerId : RANDOM.nextInt(1000)) % 1000;
        String workerPart = String.format("%03d", worker);

        // 3. 序列号（4位，每毫秒重置）
        String sequencePart = String.format("%04d", RANDOM.nextInt(10000));

        // 4. 用户ID（4位）
        String memberPart = String.format("%04d", (memberId == null ? 0 : memberId) % 10000);

        // 5. 随机数（3位）
        String randomPart = String.format("%03d", RANDOM.nextInt(1000));

        return timePart + workerPart + sequencePart + memberPart + randomPart;
    }

    /**
     * 生成订单号（方案3：UUID简化版）
     * 格式: 时间戳(8位) + UUID哈希(8位) + 随机数(6位)
     */
    public static String generateUUIDOrderNo(Long memberId) {
        // 1. 日期部分（8位）
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 2. UUID哈希部分（8位）
        UUID uuid = UUID.randomUUID();
        int hash = Math.abs(uuid.hashCode());
        String uuidPart = String.format("%08d", hash % 100000000);

        // 3. 随机数部分（6位）
        String randomPart = String.format("%06d", ThreadLocalRandom.current().nextInt(1000000));

        return datePart + uuidPart + randomPart;
    }

    /**
     * 生成订单号（方案4：可逆格式）
     * 格式: 时间戳(13位) + 用户ID(6位) + 随机数(5位) = 24位
     */
    public static String generateReversibleOrderNo(Long memberId) {
        // 1. 时间戳（13位，毫秒级）
        long timestamp = System.currentTimeMillis();

        // 2. 用户ID（6位）
        String memberPart = String.format("%06d", (memberId == null ? 0 : memberId) % 1000000);

        // 3. 随机数（5位）
        String randomPart = String.format("%05d", ThreadLocalRandom.current().nextInt(100000));

        return timestamp + memberPart + randomPart;
    }


    /**
     * 使用Redis生成分布式唯一订单号  高并发场景：推荐方案2（Redis分布式序列）
     * 格式: 日期(8位) + Redis序列(6位) + 随机数(6位)
     */
    public String generateDistributedOrderNo(Long memberId) {
        // 1. 日期部分
        String datePart = LocalDateTime.now().format(DATE_FORMATTER);

        // 2. Redis分布式序列（每天重置）
        String sequenceKey = ORDER_SEQUENCE_KEY + datePart;
        Long sequence = redisTemplate.opsForValue().increment(sequenceKey, 1);

        // 设置过期时间（2天，防止跨天问题）
        if (sequence == 1) {
            redisTemplate.expire(sequenceKey, 2, java.util.concurrent.TimeUnit.DAYS);
        }

        // 序列号格式化为6位
        String sequencePart = String.format("%06d", sequence % 1000000);

        // 3. 随机数部分（6位）
        String randomPart = String.format("%06d", ThreadLocalRandom.current().nextInt(1000000));

        // 4. 用户ID部分（可选，4位）
        String memberPart = String.format("%04d", (memberId == null ? 0 : memberId) % 10000);

        return datePart + sequencePart + randomPart;
    }
}
