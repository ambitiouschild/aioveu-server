package com.aioveu.common.rabbitmq.producer.util;


import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: MessageIdGenerator
 * @Description TODO 消息ID生成器工具类
 *                      messageId 是唯一一个“长得越长越安全”的地方 👍
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/11 20:49
 * @Version 1.0
 **/
@Component
@Slf4j
public class MessageIdGenerator {

    @Value("${spring.application.name:unknown}")
    private String applicationName;

    @Value("${server.port:8080}")
    private String serverPort;

    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    private static final AtomicInteger SEQUENCE = new AtomicInteger(0);
    private static final int MAX_SEQUENCE = 9999;

    private static String WORKER_ID = "01";

    private final Environment environment;

    public MessageIdGenerator(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    public void init() {
        try {
            // 使用IP地址后两位作为workerId
            String ip = InetAddress.getLocalHost().getHostAddress();
            String[] ipParts = ip.split("\\.");
            if (ipParts.length >= 4) {
                WORKER_ID = String.format("%02d", Integer.parseInt(ipParts[3]) % 100);
            }
        } catch (UnknownHostException e) {
            log.warn("无法获取本机IP，使用默认workerId: {}", WORKER_ID, e);
        }
    }

    /**
     * 生成通用消息ID
     * 格式：APP_TIMESTAMP_WORKER_SEQ_RANDOM
     */
    public String generateMessageId() {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        int sequence = SEQUENCE.getAndUpdate(prev -> (prev >= MAX_SEQUENCE) ? 0 : prev + 1);
        int random = ThreadLocalRandom.current().nextInt(100, 999);

        return String.format("%s_%s_%s_%04d_%d",
                getAppShortName(), timestamp, WORKER_ID, sequence, random);
    }

    /**
     * 生成消息ID
     */
    public String generateMessageIdWithUUID() {
        return "msg-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    /**
     * 生成业务消息ID
     */
    public String generateBizMessageId(String bizType, String bizNo) {
        String baseId = generateMessageId();
        return String.format("%s_%s_%s", bizType, bizNo, baseId);
    }

    /**
     * 生成支付消息ID
     */
    public String generatePaymentMessageId(String paymentNo) {
        return generateBizMessageId("PAY", paymentNo);
    }

    /**
     * 生成订单消息ID
     */
    public String generateOrderMessageId(String orderNo) {
        return generateBizMessageId("ORDER", orderNo);
    }

    /**
     * 生成退款消息ID
     */
    public String generateRefundMessageId(String refundNo) {
        return generateBizMessageId("REFUND", refundNo);
    }

    /**
     * 验证消息ID格式
     */
    public boolean validateMessageId(String messageId) {
        if (messageId == null || messageId.length() < 20) {
            return false;
        }

        // 基本格式校验
        String[] parts = messageId.split("_");
        if (parts.length < 5) {
            return false;
        }

        // 检查时间戳格式
        String timestamp = parts[1];
        if (timestamp.length() != 17) { // yyyyMMddHHmmssSSS
            return false;
        }

        try {
            Long.parseLong(timestamp);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 从消息ID中解析业务信息
     */
    public MessageIdInfo parseMessageId(String messageId) {
        if (!validateMessageId(messageId)) {
            return null;
        }

        String[] parts = messageId.split("_");
        return new MessageIdInfo(
                parts[0],
                parts[1],
                parts[2],
                Integer.parseInt(parts[3]),
                Integer.parseInt(parts[4]),
                parts.length > 5 ? parts[5] : null,
                parts.length > 6 ? parts[6] : null
        );
    }

    /**
     * 获取应用简称
     * getAppShortName()拿不到，99% 是 applicationName为 null
     * 👉 Spring 不会注入 @Value
     *
     * 👉 applicationName 一定是 null
     *
     * ✅ 必须用注入的 Bean
     */
    private String getAppShortName2() {
        if (applicationName.contains("-")) {
            String[] parts = applicationName.split("-");
            return parts[parts.length - 1].toUpperCase();
        }
        return applicationName.toUpperCase();
    }

    /*
    * 方案一（最推荐）：不用 @Value，用 Environment
    * */
    private String getAppShortName() {
        String appName = environment.getProperty("spring.application.name", "unknown");
        if (appName.contains("-")) {
            String[] parts = appName.split("-");
            return parts[parts.length - 1].toUpperCase();
        }
        return appName.toUpperCase();
    }

    /**
     * 消息ID信息DTO
     */
    @Data
//    @Builder  //方案 2：保留 MessageIdInfo，但禁止外部使用 Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MessageIdInfo {
        private String appName;      // 应用名
        private String timestamp;    // 时间戳
        private String workerId;     // 工作节点ID
        private Integer sequence;    // 序列号
        private Integer random;      // 随机数
        private String bizType;      // 业务类型
        private String bizNo;        // 业务编号
    }
}
