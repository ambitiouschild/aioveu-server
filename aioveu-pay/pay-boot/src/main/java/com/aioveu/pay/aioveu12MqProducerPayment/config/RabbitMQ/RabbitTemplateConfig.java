package com.aioveu.pay.aioveu12MqProducerPayment.config.RabbitMQ;


import com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ.RabbitTemplateInfo;
import com.aioveu.pay.aioveu12MqProducerPayment.service.RabbitMQ.impl.RabbitReturnCallbackImpl;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: RabbitTemplateConfig
 * @Description TODO RabbitTemplate 配置
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/14 18:29
 * @Version 1.0
 **/

/*
* 这些错误说明您的 Spring AMQP 版本中没有 isMandatory()和 getReturnsCallback()方法。这些方法在较新版本中可能被移除或改名了。
*
* */
@Slf4j
@Configuration
public class RabbitTemplateConfig {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitReturnCallbackImpl rabbitReturnCallback;

    @Autowired
    private ConnectionFactory connectionFactory;

    // 自己记录配置状态
    private volatile boolean mandatoryEnabled = false;
    private volatile boolean returnCallbackSet = false;
    private volatile boolean confirmCallbackSet = false;


    @PostConstruct
    public void configureRabbitTemplate() {
        // 1. 启用 mandatory 模式（正确方法）
        rabbitTemplate.setMandatory(true);

        // 2. 设置 ReturnCallback
        //这里已经设置ReturnCallback
        rabbitTemplate.setReturnsCallback(rabbitReturnCallback);

        // 3. 配置 ConfirmCallback
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.debug("消息发送确认成功: {}", correlationData != null ? correlationData.getId() : "null");
            } else {
                log.error("消息发送确认失败: {}, cause: {}",
                        correlationData != null ? correlationData.getId() : "null", cause);
            }
        });



        // 2. 检查配置是否生效
        log.info("RabbitTemplate配置完成:");
        log.info("  - 已启用mandatory模式（消息路由失败会触发ReturnCallback）");
        log.info("  - ConnectionFactory: {}", connectionFactory.getClass().getSimpleName());


        // 3. 验证配置
        validateRabbitTemplateConfig();


        // 最终日志
        log.info("RabbitTemplate配置完成: mandatory={}, 已设置ReturnCallback和ConfirmCallback",
                mandatoryEnabled);
    }


    /**
     * 验证RabbitTemplate配置
     */
    private void validateRabbitTemplateConfig() {
        try {
            // 检查必要的回调是否设置
            if (returnCallbackSet) {  // ✅ 改为使用我们自己记录的状态
                log.info("  - 已设置ReturnCallback");
            } else {
                log.warn("  - 未设置ReturnCallback，mandatory模式可能不会生效");
            }

            if (confirmCallbackSet) {  // ✅ 改为使用我们自己记录的状态
                log.info("  - 已设置ConfirmCallback");
            } else {
                log.warn("  - 未设置ConfirmCallback，无法获取Broker确认");
            }

            // 检查连接工厂
            if (connectionFactory != null) {
                log.info("  - ConnectionFactory状态: {}",
                        connectionFactory.getClass().getSimpleName());
            }

        } catch (Exception e) {
            log.error("验证RabbitTemplate配置失败", e);
        }
    }

    /**
     * 获取配置信息
     */
    public RabbitTemplateInfo getTemplateInfo() {
        RabbitTemplateInfo info = new RabbitTemplateInfo();

        // 通过反射或其他方式获取状态
        // 使用我们自己记录的状态
        info.setMandatoryEnabled(mandatoryEnabled);
        info.setConnectionFactoryType(connectionFactory.getClass().getSimpleName());
        info.setHasReturnCallback(returnCallbackSet);  // ✅ 改为使用我们自己记录的状态
        info.setHasConfirmCallback(confirmCallbackSet);  // ✅ 改为使用我们自己记录的状态

        return info;
    }



}
