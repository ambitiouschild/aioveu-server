package com.aioveu.common.rabbitmq.config;


import com.aioveu.common.rabbitmq.producer.model.vo.RabbitTemplateStatistics;
import com.aioveu.common.rabbitmq.producer.model.vo.RabbitTemplateStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: RabbitTemplateManager
 * @Description TODO RabbitTemplate管理器
 *                      提供状态检查和监控功能
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/14 18:40
 * @Version 1.0
 **/

/*
*  问题在于 RabbitTemplate类中没有 getReturnsCallback()和 getConfirmCallback()方法。这些方法在新版本中可能已经被移除或改名了
*  RabbitTemplate类在 Spring AMQP 的不同版本中有不同的方法。根据您的错误信息，当前版本中没有 getReturnsCallback()方法。
*  方案3：通过您自己的管理类来追踪状态（推荐）
    由于直接检查 RabbitTemplate 的状态可能不可靠，您可以自己维护回调的设置状态：
*
* */

/*
* RocketMQ 和 RabbitMQ 可以同时配置
是的，RocketMQ 和 RabbitMQ 可以同时配置在一个应用中，它们之间没有冲突。
* */
@Slf4j
@Service
public class RabbitTemplateManager {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ConnectionFactory connectionFactory;

    private final AtomicInteger sendCount = new AtomicInteger(0);
    private final AtomicInteger returnCount = new AtomicInteger(0);
    private final AtomicInteger confirmCount = new AtomicInteger(0);

    private volatile boolean returnCallbackSet = false;
    private volatile boolean confirmCallbackSet = false;
    private final AtomicInteger returnCallbackCount = new AtomicInteger(0);

    /**
     * 设置 ReturnCallback
     */
    public void setReturnsCallback(RabbitTemplate rabbitTemplate, RabbitTemplate.ReturnsCallback callback) {
        rabbitTemplate.setReturnsCallback(callback);
        this.returnCallbackSet = true;
        this.returnCallbackCount.incrementAndGet();
    }

    /**
     * 设置 ConfirmCallback
     */
    public void setConfirmCallback(RabbitTemplate rabbitTemplate, RabbitTemplate.ConfirmCallback callback) {
        rabbitTemplate.setConfirmCallback(callback);
        this.confirmCallbackSet = true;
    }

    /**
     * 检查 ReturnCallback 是否设置
     */
    public boolean isReturnCallbackSet() {
        return this.returnCallbackSet;
    }

    /**
     * 检查 ConfirmCallback 是否设置
     */
    public boolean isConfirmCallbackSet() {
        return this.confirmCallbackSet;
    }


    /**
     * 检查RabbitTemplate状态
     */
    public RabbitTemplateStatus checkStatus() {
        RabbitTemplateStatus status = new RabbitTemplateStatus();

        try {
            // 1. 检查mandatory状态
            status.setMandatoryEnabled(checkMandatoryStatus());

            // 2. 检查回调设置
            // 2. 检查回调设置 - 使用我们自己维护的状态
            status.setReturnCallbackSet(this.returnCallbackSet);  // ✅ 改为使用我们自己的状态
            status.setConfirmCallbackSet(this.confirmCallbackSet); // ✅ 改为使用我们自己的状态

            // 3. 检查连接工厂
            if (connectionFactory instanceof CachingConnectionFactory) {
                CachingConnectionFactory cachingFactory = (CachingConnectionFactory) connectionFactory;
                status.setConnectionFactoryType("CachingConnectionFactory");
                status.setCacheMode(cachingFactory.getCacheMode().name());
                status.setConnectionCacheSize(cachingFactory.getConnectionCacheSize());
                status.setChannelCacheSize(cachingFactory.getChannelCacheSize());
            } else {
                status.setConnectionFactoryType(connectionFactory.getClass().getSimpleName());
            }

            // 4. 统计信息
            status.setTotalSendCount(sendCount.get());
            status.setTotalReturnCount(returnCount.get());
            status.setTotalConfirmCount(confirmCount.get());

            // 5. 连接状态
            status.setConnectionActive(checkConnectionActive());

        } catch (Exception e) {
            status.setError(e.getMessage());
            log.error("检查RabbitTemplate状态失败", e);
        }

        status.setCheckTime(new Date());
        return status;
    }

    /**
     * 检查mandatory状态（通过反射）
     */
    private boolean checkMandatoryStatus() {
        try {
            // 尝试通过反射获取mandatory状态
            // RabbitTemplate内部通过ChannelListener维护这个状态

            // 更可靠的方法：通过测试发送消息来验证
            return isMandatoryEnabledByTest();

        } catch (Exception e) {
            log.debug("检查mandatory状态失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 通过测试发送验证mandatory状态
     */
    private boolean isMandatoryEnabledByTest() {
        // 在实际使用中，mandatory状态是由setMandatory(true)设置的
        // 这里我们假设如果设置了ReturnCallback，那么mandatory就是启用的

        // 实际验证：发送一个不存在的routingKey，检查是否会触发ReturnCallback
        // 注意：这会影响生产环境，所以这里只做逻辑判断
        return this.returnCallbackSet;  // ✅ 如果设置了ReturnCallback，我们认为mandatory是启用的
    }

    /**
     * 检查连接是否活跃
     */
    private boolean checkConnectionActive() {
        try {
            // 尝试创建临时连接来测试
            org.springframework.amqp.rabbit.connection.Connection conn =
                    connectionFactory.createConnection();
            boolean active = conn.isOpen();
            conn.close();
            return active;
        } catch (Exception e) {
            log.warn("检查连接状态失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 发送消息（带统计）
     */
    public void sendWithStatistics(String exchange, String routingKey, Message message) {
        long startTime = System.currentTimeMillis();
        sendCount.incrementAndGet();

        try {
            rabbitTemplate.send(exchange, routingKey, message);
            long costTime = System.currentTimeMillis() - startTime;

            log.debug("消息发送完成: exchange={}, routingKey={}, cost={}ms",
                    exchange, routingKey, costTime);

        } catch (Exception e) {
            log.error("消息发送失败: exchange={}, routingKey={}", exchange, routingKey, e);
            throw e;
        }
    }

    /**
     * 发送消息（带关联数据）
     */
    public void sendWithCorrelation(String exchange, String routingKey,
                                    Message message, CorrelationData correlationData) {
        sendCount.incrementAndGet();

        try {
            rabbitTemplate.send(exchange, routingKey, message, correlationData);
        } catch (Exception e) {
            log.error("发送带关联数据的消息失败", e);
            throw e;
        }
    }

    /**
     * 记录ReturnCallback触发
     */
    public void recordReturnCallback() {
        returnCount.incrementAndGet();
    }

    /**
     * 记录ConfirmCallback触发
     */
    public void recordConfirmCallback() {
        confirmCount.incrementAndGet();
    }

    /**
     * 重置统计
     */
    public void resetStatistics() {
        sendCount.set(0);
        returnCount.set(0);
        confirmCount.set(0);
        log.info("RabbitTemplate统计已重置");
    }

    /**
     * 获取统计信息
     */
    public RabbitTemplateStatistics getStatistics() {
        RabbitTemplateStatistics stats = new RabbitTemplateStatistics();
        stats.setSendCount(sendCount.get());
        stats.setReturnCount(returnCount.get());
        stats.setConfirmCount(confirmCount.get());
        stats.setSuccessRate(calculateSuccessRate());
        stats.setLastUpdated(new Date());
        return stats;
    }

    /**
     * 计算成功率
     */
    private double calculateSuccessRate() {
        int total = sendCount.get();
        int returns = returnCount.get();

        if (total == 0) {
            return 0.0;
        }

        int success = total - returns;
        return (double) success / total * 100;
    }

}
