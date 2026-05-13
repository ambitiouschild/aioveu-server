package com.aioveu.oms.aioveu11MqConsumer.config;

import org.apache.rocketmq.client.ClientConfig;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.remoting.RPCHook;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: RocketMQAdminConfig
 * @Description TODO 创建一个配置类来配置 DefaultMQAdminExt
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/12 17:23
 * @Version 1.0
 **/
@Slf4j
@Configuration
public class RocketMQAdminConfig{


    @Value("${rocketmq.name-server:127.0.0.1:9876}")
    private String nameServer;


    @Value("${rocketmq.admin.access-key:#{null}}")
    private String accessKey;

    @Value("${rocketmq.admin.secret-key:#{null}}")
    private String secretKey;


    /**
     * RocketMQ 管理客户端
     */
    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public DefaultMQAdminExt defaultMQAdminExt() {
        try {
            DefaultMQAdminExt adminExt = new DefaultMQAdminExt();
            adminExt.setNamesrvAddr(nameServer);

            // 如果配置了ACL，设置RPCHook
            if (accessKey != null && secretKey != null) {
                adminExt.setVipChannelEnabled(false);
            }

            // 设置超时时间
            adminExt.setAdminExtGroup("admin-monitor-group");
            adminExt.setInstanceName("admin-instance-" + System.currentTimeMillis());

            log.info("RocketMQ Admin 客户端初始化完成");
            return adminExt;

        } catch (Exception e) {
            log.error("初始化 RocketMQ Admin 客户端失败", e);
            throw new RuntimeException("初始化 RocketMQ Admin 客户端失败", e);
        }
    }

    /**
     * 获取唯一的实例名
     */
    private String getInstanceName(String group) {
        return String.format("%s_%s_%d",
                group,
                System.getProperty("rocketmq.client.name", "default"),
                System.currentTimeMillis() % 10000
        );
    }
}
