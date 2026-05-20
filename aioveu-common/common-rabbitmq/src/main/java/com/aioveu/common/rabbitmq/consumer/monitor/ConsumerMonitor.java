package com.aioveu.common.rabbitmq.consumer.monitor;


import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;  // ✅ 正确导入TimeUnit
import java.util.Properties;

/**
 * @ClassName: ConsumerMonitor
 * @Description TODO RabbitMQ消费者监控组件
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/12 17:42
 * @Version 1.0
 **/
/**
 * RabbitMQ消费者监控组件
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumerMonitor {

    // 阈值配置
    private static final double SUCCESS_RATE_THRESHOLD = 0.95;  // 95%
    private static final long DELAY_THRESHOLD_MS = 10000;      // 10秒
    private static final int DLQ_THRESHOLD = 0;                // 死信队列警告阈值

    private final MeterRegistry meterRegistry;
    private final RabbitAdmin rabbitAdmin;
    private final ConnectionFactory connectionFactory;

    /**
     * 每30秒执行一次监控检查
     */
    @Scheduled(fixedDelay = 30000)
    public void monitorConsumer() {
        try {
            // 1. 检查消费成功率
            double successRate = getConsumeSuccessRate();
            if (successRate < SUCCESS_RATE_THRESHOLD) {
                sendAlert("消费端成功率低: " + String.format("%.2f%%", successRate * 100));
            }

            // 2. 检查消费延迟
            long delayMs = getConsumeDelay();
            if (delayMs > DELAY_THRESHOLD_MS) {
                sendAlert("消费延迟过高: " + delayMs + "ms");
            }

            // 3. 检查死信队列
            int dlqCount = getDlqMessageCount();
            if (dlqCount > DLQ_THRESHOLD) {
                sendAlert("死信队列有消息: " + dlqCount);
            }

            // 4. 检查消费者连接
            int consumerConnections = getConsumerConnections();
            if (consumerConnections == 0) {
                sendAlert("消费者无连接");
            }

        } catch (Exception e) {
            log.error("消费者监控异常", e);
        }
    }

    /**
     * 获取消费成功率
     */
    private double getConsumeSuccessRate() {
        try {
            // 方案A: 从Micrometer获取
            return meterRegistry.get("rabbitmq.consume.success.rate")
                    .counter()
                    .count();

            // 方案B: 自定义统计
            // 这里可以返回你实际的统计逻辑
        } catch (Exception e) {
            log.warn("获取消费成功率失败", e);
            return 1.0; // 默认100%
        }
    }

    /**
     * 获取消费延迟
     */
    private long getConsumeDelay() {
        try {
            // 方案A: 从Micrometer获取
            return (long) meterRegistry.get("rabbitmq.consume.delay")
                    .timer()
                    .mean(TimeUnit.MILLISECONDS);  // 使用Java标准库的TimeUnit

            // 方案B: 自定义计算
            // 这里可以返回你实际的延迟计算逻辑
        } catch (Exception e) {
            log.warn("获取消费延迟失败", e);
            return 0L;
        }
    }

    /**
     * 获取死信队列消息数量
     */
    private int getDlqMessageCount() {
        try {
            Properties queueProperties = rabbitAdmin.getQueueProperties("your.dlq.queue");
            if (queueProperties != null) {
                return Integer.parseInt(queueProperties.get("QUEUE_MESSAGE_COUNT").toString());
            }
            return 0;
        } catch (Exception e) {
            log.warn("获取死信队列消息数失败", e);
            return 0;
        }
    }

    /**
     * 获取消费者连接状态
     */
    private int getConsumerConnections() {
        Connection connection = null;
        try {
            // 尝试创建连接测试连接状态
            connection = connectionFactory.createConnection();

            if (connection != null && connection.isOpen()) {
                // 如果是CachingConnectionFactory，可以获取更多信息
                if (connectionFactory instanceof CachingConnectionFactory cachingFactory) {
                    // 获取连接缓存大小
                    int cacheSize = cachingFactory.getChannelCacheSize();
                    log.debug("连接缓存大小: {}", cacheSize);
                    return cacheSize;
                }
                return 1;  // 连接正常
            }
            return 0;

        } catch (Exception e) {
            log.debug("连接测试失败", e);
            return 0;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    // 忽略关闭异常
                }
            }
        }
    }

    /**
     * 发送告警
     */
    private void sendAlert(String message) {
        // 方案A: 记录错误日志
        log.error("RabbitMQ监控告警: {}", message);

        // 方案B: 发送邮件通知
        // emailService.sendAlert("RabbitMQ告警", message);

        // 方案C: 发送到监控系统
        // monitorService.recordAlert("rabbitmq", message);
    }
}
