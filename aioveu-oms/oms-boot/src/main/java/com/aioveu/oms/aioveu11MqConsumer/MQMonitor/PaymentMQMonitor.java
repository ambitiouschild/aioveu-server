package com.aioveu.oms.aioveu11MqConsumer.MQMonitor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @ClassName: PaymentMQMonitor
 * @Description TODO 监控和告警
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/11 18:42
 * @Version 1.0
 **/
@Component
@Slf4j
public class PaymentMQMonitor {

    @Autowired
    private DefaultMQAdminExt mqAdminExt;

    @Scheduled(fixedDelay = 60000)  // 每分钟检查一次
    public void monitorMQStatus() {
        try {
            // 1. 检查消息积压
            checkMessageBacklog();

            // 2. 检查消费延迟
            checkConsumeDelay();

            // 3. 检查死信队列
            checkDeadLetterQueue();

        } catch (Exception e) {
            log.error("监控MQ状态异常", e);
        }
    }


    private void checkMessageBacklog() throws Exception {
        String[] topics = {"payment_success_topic", "payment_failed_topic"};

        for (String topic : topics) {
            TopicStatsTable stats = mqAdminExt.examineTopicStats(topic);

            long totalBacklog = 0;
            for (Map.Entry<MessageQueue, TopicOffset> entry : stats.getOffsetTable().entrySet()) {
                long diff = entry.getValue().getMaxOffset() - entry.getValue().getMinOffset();
                totalBacklog += diff;
            }

            // 记录指标
            metricsService.recordGauge("mq.backlog", totalBacklog, "topic", topic);

            // 告警
            if (totalBacklog > 1000) {
                alertService.sendAlert("MQ积压告警",
                        String.format("topic: %s, 积压消息数: %d", topic, totalBacklog));
            }
        }
    }
}
