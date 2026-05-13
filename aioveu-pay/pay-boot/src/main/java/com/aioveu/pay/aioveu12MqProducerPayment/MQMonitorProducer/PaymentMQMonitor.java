package com.aioveu.pay.aioveu12MqProducerPayment.MQMonitorProducer;

import com.aioveu.pay.aioveu12MqProducerPayment.service.MQAlertService;
import com.aioveu.pay.aioveu12MqProducerPayment.service.MQMetricsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.protocol.admin.TopicOffset;
import org.apache.rocketmq.remoting.protocol.admin.TopicStatsTable;
import org.apache.rocketmq.remoting.protocol.body.ClusterInfo;
import org.apache.rocketmq.remoting.protocol.body.Connection;
import org.apache.rocketmq.remoting.protocol.body.ConsumerConnection;
import org.apache.rocketmq.remoting.protocol.body.ConsumerRunningInfo;
import org.apache.rocketmq.remoting.protocol.heartbeat.SubscriptionData;
import org.apache.rocketmq.remoting.protocol.route.TopicRouteData;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: PaymentMQMonitor
 * @Description TODO 监控和告警  RocketMQ 监控组件
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/11 18:42
 * @Version 1.0
 **/
@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentMQMonitor {

    @Autowired
    private DefaultMQAdminExt mqAdminExt;

    private final MQMetricsService metricsService;
    private final MQAlertService alertService;

    @Value("${rocketmq.monitor.topics:payment_success_topic,payment_failed_topic}")
    private String[] monitorTopics;

// 监控指标包括：
// 1. 消息发送成功率
// 2. 消息积压情况
// 3. 发送延迟
// 4. 重试次数
/*
* 生产端监控重点：
✅ 消息发送成功率
✅ 发送失败率
✅ 消息积压数量
✅ 发送延迟时间
✅ 补偿任务执行状态
*
* */

    @Value("${rocketmq.monitor.consumer-groups:order_service_group}")
    private String[] monitorConsumerGroups;

    @Value("${rocketmq.monitor.backlog-threshold:1000}")
    private long backlogThreshold;

    @Value("${rocketmq.monitor.delay-threshold:10000}")
    private long delayThreshold;

    @Value("${rocketmq.monitor.enabled:true}")
    private boolean monitorEnabled;


    // 记录告警状态，避免重复告警
    private final Map<String, Long> lastAlertTime = new ConcurrentHashMap<>();
    private static final long ALERT_INTERVAL_MS = 5 * 60 * 1000; // 5分钟

    @Scheduled(fixedDelay = 60000)  // 每分钟检查一次
    public void monitorMQStatus() {

        if (!monitorEnabled) {
            return;
        }


        try {

            log.debug("开始执行MQ监控任务...");

            // 1. 检查消息积压
            checkMessageBacklog();

            // 2. 检查消费延迟
            checkConsumeDelay();

            // 3. 检查死信队列
            checkDeadLetterQueue();

            // 4. 检查集群状态
            checkClusterStatus();

            // 5. 检查消费者状态
            checkConsumerStatus();

            log.debug("MQ监控任务执行完成");

        } catch (Exception e) {
            log.error("监控MQ状态异常", e);
        }
    }

    /**
     * 检查消息积压
     */
    private void checkMessageBacklog() {
        try {
            for (String topic : monitorTopics) {
                try {
                    TopicStatsTable stats = mqAdminExt.examineTopicStats(topic);

                    long totalBacklog = 0;
                    Map<MessageQueue, Long> queueBacklog = new HashMap<>();

                    for (Map.Entry<MessageQueue, TopicOffset> entry : stats.getOffsetTable().entrySet()) {
                        MessageQueue mq = entry.getKey();
                        TopicOffset offset = entry.getValue();

                        // 计算队列积压
                        long backlog = offset.getMaxOffset() - offset.getMinOffset();
                        totalBacklog += backlog;
                        queueBacklog.put(mq, backlog);
                    }

                    // 记录指标
                    metricsService.recordGauge("mq.topic.backlog", totalBacklog,
                            "topic", topic);

                    // 记录每个队列的积压
                    for (Map.Entry<MessageQueue, Long> entry : queueBacklog.entrySet()) {
                        metricsService.recordGauge("mq.queue.backlog", entry.getValue(),
                                "topic", topic,
                                "broker", entry.getKey().getBrokerName(),
                                "queue", String.valueOf(entry.getKey().getQueueId()));
                    }

                    // 告警
                    if (totalBacklog > backlogThreshold) {
                        sendAlert("MQ积压告警",
                                String.format("Topic: %s, 积压消息数: %d (阈值: %d)",
                                        topic, totalBacklog, backlogThreshold),
                                "BACKLOG_HIGH");
                    }

                } catch (Exception e) {
                    log.warn("检查Topic积压失败: {}", topic, e);
                }
            }

        } catch (Exception e) {
            log.error("检查消息积压异常", e);
        }
    }

    /**
     * 检查消费延迟 检查消费者连接
     */
    private void checkConsumeDelay() {
        try {
            for (String consumerGroup : monitorConsumerGroups) {
                try {
                    // 获取消费者连接信息
                    ConsumerConnection consumerConnection = mqAdminExt.examineConsumerConnectionInfo(consumerGroup);

                    if (consumerConnection == null || consumerConnection.getConnectionSet().isEmpty()) {
                        log.warn("消费者组无连接: {}", consumerGroup);
                        continue;
                    }

                    // 检查每个连接的消费延迟
                    /*
                    *   在较新版本的 RocketMQ 中：
                        ConsumerConnection.getConnectionSet()返回的是 Set<Connection>对象
                        而不是你代码中使用的 Set<String>
                    *
                    * */

                    /*
                    * // 如果使用的是 RocketMQ 4.x 版本，可能需要：旧版本的获取方式
                            Set<String> clientIds = consumerConnection.getConnectionSet();
                            for (String clientId : clientIds) {
                                // 处理逻辑
                            }
                    *
                    * */

                    /*
                    * 不同版本的差异
                    * 版本    getConnectionSet()返回类型       说明
                    * 4.x     Set<String>               返回客户端ID集合
                    * 5.x     Set<Connection>           返回连接对象集合
                    *
                    * */
                    // 使用正确的类型遍历
                    Set<Connection> connections = consumerConnection.getConnectionSet();
                    for (Connection connection : consumerConnection.getConnectionSet()) {
                        try {

                            String clientId = connection.getClientId();

                            ConsumerRunningInfo runningInfo = mqAdminExt.getConsumerRunningInfo(
                                    consumerGroup, clientId, false);

                            if (runningInfo != null && runningInfo.getSubscriptionSet() != null) {
                                for (SubscriptionData subscription : runningInfo.getSubscriptionSet()) {
                                    String topic = subscription.getTopic();

                                    // 获取消费进度
                                    long consumeOffset = getConsumerOffset(consumerGroup, topic);
                                    long maxOffset = getTopicMaxOffset(topic);

                                    if (maxOffset > 0) {
                                        long delay = maxOffset - consumeOffset;

                                        // 记录指标
                                        metricsService.recordGauge("mq.consumer.delay", delay,
                                                "consumerGroup", consumerGroup,
                                                "topic", topic,
                                                "clientId", clientId);

                                        // 告警
                                        if (delay > delayThreshold) {
                                            sendAlert("消费延迟告警",
                                                    String.format("消费者组: %s, Topic: %s, 延迟消息数: %d (阈值: %d)",
                                                            consumerGroup, topic, delay, delayThreshold),
                                                    "DELAY_HIGH");
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            log.warn("检查消费者延迟失败: group={}, client={}", consumerGroup, connection.getClientId(), e);
                        }
                    }

                } catch (Exception e) {
                    log.warn("检查消费延迟失败: {}", consumerGroup, e);
                }
            }

        } catch (Exception e) {
            log.error("检查消费延迟异常", e);
        }
    }

    /**
     * 检查死信队列
     */
    private void checkDeadLetterQueue() {
        try {
            for (String consumerGroup : monitorConsumerGroups) {
                try {
                    // 死信队列的Topic格式: %DLQ% + consumerGroup
                    String dlqTopic = "%DLQ%" + consumerGroup;

                    // 检查死信队列是否存在
                    ClusterInfo clusterInfo = mqAdminExt.examineBrokerClusterInfo();
                    boolean dlqExists = clusterInfo.getBrokerAddrTable().values().stream()
                            .anyMatch(brokerData -> {
                                try {
                                    mqAdminExt.examineTopicStats(dlqTopic);
                                    return true;
                                } catch (Exception e) {
                                    return false;
                                }
                            });

                    if (dlqExists) {
                        // 获取死信队列消息数
                        try {
                            TopicStatsTable dlqStats = mqAdminExt.examineTopicStats(dlqTopic);
                            long dlqCount = dlqStats.getOffsetTable().values().stream()
                                    .mapToLong(offset -> offset.getMaxOffset() - offset.getMinOffset())
                                    .sum();

                            // 记录指标
                            metricsService.recordGauge("mq.dlq.count", dlqCount,
                                    "consumerGroup", consumerGroup);

                            // 告警（有死信就告警）
                            if (dlqCount > 0) {
                                sendAlert("死信队列告警",
                                        String.format("消费者组: %s, 死信消息数: %d", consumerGroup, dlqCount),
                                        "DLQ_NOT_EMPTY");
                            }

                        } catch (Exception e) {
                            log.warn("检查死信队列统计失败: {}", dlqTopic, e);
                        }
                    }

                } catch (Exception e) {
                    log.warn("检查死信队列失败: {}", consumerGroup, e);
                }
            }

        } catch (Exception e) {
            log.error("检查死信队列异常", e);
        }
    }

    /**
     * 检查集群状态
     */
    private void checkClusterStatus() {
        try {
            ClusterInfo clusterInfo = mqAdminExt.examineBrokerClusterInfo();

            int brokerCount = clusterInfo.getBrokerAddrTable().size();
            int namesrvCount = clusterInfo.getClusterAddrTable().size();

            // 记录指标
            metricsService.recordGauge("mq.cluster.broker.count", brokerCount);
            metricsService.recordGauge("mq.cluster.namesrv.count", namesrvCount);

            // 检查是否有宕机的Broker
            // 这里可以添加更复杂的健康检查逻辑

        } catch (Exception e) {
            log.error("检查集群状态异常", e);
        }
    }

    /**
     * 检查消费者状态
     */
    private void checkConsumerStatus() {
        try {
            for (String consumerGroup : monitorConsumerGroups) {
                try {
                    ConsumerConnection connection = mqAdminExt.examineConsumerConnectionInfo(consumerGroup);

                    if (connection == null) {
                        // 消费者组不存在或已下线
                        sendAlert("消费者状态告警",
                                String.format("消费者组不存在或已下线: %s", consumerGroup),
                                "CONSUMER_OFFLINE");
                        continue;
                    }

                    int connectionCount = connection.getConnectionSet().size();

                    // 记录指标
                    metricsService.recordGauge("mq.consumer.connection.count", connectionCount,
                            "consumerGroup", consumerGroup);

                    // 检查是否有消费者连接
                    if (connectionCount == 0) {
                        sendAlert("消费者状态告警",
                                String.format("消费者组无连接: %s", consumerGroup),
                                "CONSUMER_NO_CONNECTION");
                    }

                } catch (Exception e) {
                    log.warn("检查消费者状态失败: {}", consumerGroup, e);
                }
            }

        } catch (Exception e) {
            log.error("检查消费者状态异常", e);
        }
    }

    /**
     * 发送告警（带防重）
     */
    private void sendAlert(String title, String message, String alertKey) {
        long now = System.currentTimeMillis();
        Long lastTime = lastAlertTime.get(alertKey);

        // 检查是否在防重间隔内
        if (lastTime != null && (now - lastTime) < ALERT_INTERVAL_MS) {
            log.debug("告警防重: {}, 上次告警时间: {}", alertKey, new Date(lastTime));
            return;
        }

        try {
            alertService.sendAlert(title, message);
            lastAlertTime.put(alertKey, now);
            log.warn("发送告警: {} - {}", title, message);
        } catch (Exception e) {
            log.error("发送告警失败", e);
        }
    }

    /*
     * minOffset方法签名发生了变化。在 RocketMQ 5.x 版本中，minOffset方法的参数发生了变化。
     * 在 RocketMQ 4.x 版本中：
     * long minOffset(String topic, String consumerGroup, int queueId) throws ...;
     * 在 RocketMQ 5.x 版本中:
     * long minOffset(MessageQueue mq) throws ...;
     * */

    /**
     * 获取消费者偏移量 （RocketMQ 5.x 版本）
     */
    private long getConsumerOffset(String consumerGroup, String topic) throws Exception {
        try {
            // 5.x 版本需要先获取 MessageQueue
            TopicRouteData routeData = mqAdminExt.examineTopicRouteInfo(topic);
            if (routeData == null || routeData.getQueueDatas().isEmpty()) {
                return 0;
            }

            // 获取第一个队列
            MessageQueue mq = new MessageQueue(topic,
                    routeData.getQueueDatas().get(0).getBrokerName(), 0);

            // 获取消费者偏移量
            return mqAdminExt.minOffset(mq);
        } catch (Exception e) {
            log.warn("获取消费者偏移量失败: group={}, topic={}", consumerGroup, topic, e);
            return 0;
        }
    }

    /**
     * 获取Topic最大偏移量
     */
    private long getTopicMaxOffset(String topic) throws Exception {
        try {
            TopicStatsTable stats = mqAdminExt.examineTopicStats(topic);
            return stats.getOffsetTable().values().stream()
                    .mapToLong(TopicOffset::getMaxOffset)
                    .max()
                    .orElse(0);
        } catch (Exception e) {
            log.warn("获取Topic最大偏移量失败: topic={}", topic, e);
            return 0;
        }
    }



}
