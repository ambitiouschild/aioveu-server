package com.aioveu.oms.aioveu09MqDeadLetter.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName: DlqMonitorJob
 * @Description TODO  最简单可用的告警（Spring 定时任务）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/28 17:41
 * @Version 1.0
 **/
@Component
@Slf4j
public class DlqMonitorJob {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    //如果你想「配置化」（强烈推荐）
    /*
    * mq:
      monitor:
        queues:
          - payment.success.queue
          - payment.success.queue.dlq
    *
    * */
    //1️给默认值（防止 yml 忘了配）
    @Value("${mq.monitor.queues:payment.success.queue,payment.success.queue.dlq}")
    private List<String> monitorQueues;


    /**
     * 同时监控的队列
     */
    private static final List<String> MONITOR_QUEUES = List.of(
            "payment.success.queue",
            "payment.success.queue.dlq"
    );

    @Scheduled(fixedDelay = 60_000)
    public void monitorDlq() {

        if (monitorQueues == null || monitorQueues.isEmpty()) {
            log.warn("【DLQ监控】未配置监控队列");
            return;
        }

        for (String queueName : monitorQueues) {

            try {
                int count = rabbitTemplate.execute(channel ->
                        channel.queueDeclarePassive(queueName)
                                .getMessageCount()
                );

                if (count > 0) {
                    log.error("🚨 MQ 队列告警 | queue={}, 消息数={}",
                            queueName, count);
                    sendAlert(queueName, count);
                }

            } catch (Exception e) {
                log.error("【DLQ监控】查询队列失败, queue={}", queueName, e);
            }
        }
    }

    /**
     * 发送告警（钉钉 / 企业微信 / 短信）
     */
    private void sendAlert(String queueName, int count) {
        String alert = String.format(
                """
                🚨 MQ 死信告警
                队列：%s
                消息数量：%d
                环境：PROD
                时间：%s
                """,
                queueName,
                count,
                java.time.LocalDateTime.now()
        );

        log.error(alert);

        // TODO: 实际发送告警
        // dingTalkService.send(alert);
        // weChatWorkService.send(alert);
    }

    /*

    Prometheus + Grafana（推荐）
    RabbitMQ Exporter 指标
    rabbitmq_queue_messages{queue="payment.success.queue.dlq"} > 0
    * 🚨 MQ 死信告警
            队列：payment.success.queue.dlq
            消息数量：3
            环境：PROD
            时间：2026-05-28 17:00
    *
    *
    * */
}
