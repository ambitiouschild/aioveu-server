package com.aioveu.tenant.handler;

import com.aioveu.tenant.aioveu02User.service.UserOnlineService;
import com.aioveu.tenant.aioveu12WebSocket.publisher.WebSocketPublisher;
import com.aioveu.tenant.aioveu12WebSocket.topic.WebSocketTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @ClassName: OnlineUserJobHandler
 * @Description TODO 在线用户定时任务
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 21:02
 * @Version 1.0
 **/
@Component
@Slf4j
@RequiredArgsConstructor
public class OnlineUserJobHandler {

    private final UserOnlineService userOnlineService;
    private final WebSocketPublisher webSocketPublisher;

    // 每3分钟统计一次在线用户数，减少服务器压力
    @Scheduled(cron = "0 */3 * * * ?")
    public void execute() {
        log.info("定时任务：统计在线用户数");
        // 推送在线用户数量到新主题
        int count = userOnlineService.getOnlineUserCount();
        webSocketPublisher.publish(WebSocketTopics.TOPIC_ONLINE_COUNT, count);
    }
}
