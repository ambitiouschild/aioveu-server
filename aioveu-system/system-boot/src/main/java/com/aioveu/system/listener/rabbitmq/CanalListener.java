package com.aioveu.system.listener.rabbitmq;

import com.aioveu.system.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Description: TODO Canal + RabbitMQ 监听数据库数据变化
 * @Author: 雒世松
 * @Date: 2025/6/5 17:15
 * @param
 * @return:
 **/

@Component
@Slf4j
@RequiredArgsConstructor
public class CanalListener {

    private final SysMenuService menuService;

    //@RabbitListener(queues = "canal.queue")
    public void handleDataChange() {

    }
}
