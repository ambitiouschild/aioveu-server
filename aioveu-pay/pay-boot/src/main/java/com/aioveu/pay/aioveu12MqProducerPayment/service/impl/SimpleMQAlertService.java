package com.aioveu.pay.aioveu12MqProducerPayment.service.impl;


import com.aioveu.pay.aioveu12MqProducerPayment.service.MQAlertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ClassName: SimpleMQAlertService
 * @Description TODO 简单的告警服务实现（只记录日志）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/12 18:18
 * @Version 1.0
 **/
@Slf4j
@Service
public class SimpleMQAlertService implements MQAlertService {

    @Override
    public void sendAlert(String title, String message) {
        log.warn("【告警】{}: {}", title, message);
    }

    @Override
    public void sendAlert(String title, String message, String level) {
        switch (level.toUpperCase()) {
            case "ERROR":
                log.error("【告警-ERROR】{}: {}", title, message);
                break;
            case "WARN":
                log.warn("【告警-WARN】{}: {}", title, message);
                break;
            case "INFO":
                log.info("【告警-INFO】{}: {}", title, message);
                break;
            default:
                log.warn("【告警-{}】{}: {}", level, title, message);
        }
    }
}
