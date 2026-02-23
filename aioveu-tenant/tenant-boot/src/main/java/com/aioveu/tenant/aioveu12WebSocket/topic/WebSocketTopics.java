package com.aioveu.tenant.aioveu12WebSocket.topic;

/**
 * @ClassName: WebSocketTopics
 * @Description TODO
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/23 12:58
 * @Version 1.0
 **/

public final class WebSocketTopics {

    private WebSocketTopics() {
    }

    public static final String TOPIC_DICT = "/topic/dict";
    public static final String TOPIC_ONLINE_COUNT = "/topic/online-count";
    public static final String TOPIC_PUBLIC = "/topic/public";

    public static final String USER_QUEUE_MESSAGES = "/queue/messages";
    public static final String USER_QUEUE_MESSAGE = "/queue/message";
    public static final String USER_QUEUE_GREETING = "/queue/greeting";
}
