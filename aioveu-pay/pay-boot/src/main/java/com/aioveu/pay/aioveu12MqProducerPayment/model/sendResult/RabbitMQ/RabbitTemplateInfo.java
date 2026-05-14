package com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName: RabbitTemplateInfo
 * @Description TODO  RabbitMQ配置信息类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/14 18:37
 * @Version 1.0
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RabbitTemplateInfo {

    private boolean mandatoryEnabled;
    private String connectionFactoryType;
    private boolean hasReturnCallback;
    private boolean hasConfirmCallback;
    private Date configTime = new Date();

    public String getSummary() {
        return String.format(
                "RabbitTemplate配置: mandatory=%s, ReturnCallback=%s, ConfirmCallback=%s, 连接工厂=%s",
                mandatoryEnabled, hasReturnCallback, hasConfirmCallback, connectionFactoryType
        );
    }
}
