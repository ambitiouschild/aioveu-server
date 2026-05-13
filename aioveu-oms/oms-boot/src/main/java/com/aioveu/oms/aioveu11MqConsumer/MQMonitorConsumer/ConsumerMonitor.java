package com.aioveu.oms.aioveu11MqConsumer.MQMonitorConsumer;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @ClassName: ConsumerMonitor
 * @Description TODO ConsumerMonitor
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/12 17:42
 * @Version 1.0
 **/
@Component
public class ConsumerMonitor {

    @Scheduled(fixedDelay = 30000)
    public void monitorConsumer() {
        // 1. 检查消费成功率
        if (consumeSuccessRate < 95%) {
            alert("消费端成功率低: " + consumeSuccessRate);
        }

        // 2. 检查消费延迟
        if (consumeDelay > 10000) {  // 10秒
            alert("消费延迟过高: " + consumeDelay + "ms");
        }

        // 3. 检查死信队列
        if (dlqCount > 0) {
            alert("死信队列有消息: " + dlqCount);
        }

        // 4. 检查消费者连接
        if (consumerConnections == 0) {
            alert("消费者无连接");
        }
    }
}
}
