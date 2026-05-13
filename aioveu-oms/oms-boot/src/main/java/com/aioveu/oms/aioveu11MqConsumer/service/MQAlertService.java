package com.aioveu.oms.aioveu11MqConsumer.service;


/**
 * @ClassName: MQAlertService
 * @Description TODO  告警服务接口
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/12 17:29
 * @Version 1.0
 **/

public interface MQAlertService {

    /**
     * 发送告警
     */
    void sendAlert(String title, String message);

    /**
     * 发送告警（带级别）
     */
    void sendAlert(String title, String message, String level);
}
