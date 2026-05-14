package com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName: RabbitTemplateStatus
 * @Description TODO  RabbitTemplate状态类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/14 18:42
 * @Version 1.0
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RabbitTemplateStatus {

    private boolean mandatoryEnabled;
    private boolean returnCallbackSet;
    private boolean confirmCallbackSet;
    private String connectionFactoryType;
    private String cacheMode;
    private Integer connectionCacheSize;
    private Integer channelCacheSize;
    private Integer totalSendCount;
    private Integer totalReturnCount;
    private Integer totalConfirmCount;
    private boolean connectionActive;
    private String error;
    private Date checkTime;

    public String getStatusSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("RabbitTemplate状态检查:\n");
        sb.append("  mandatory模式: ").append(mandatoryEnabled ? "启用" : "禁用").append("\n");
        sb.append("  ReturnCallback: ").append(returnCallbackSet ? "已设置" : "未设置").append("\n");
        sb.append("  ConfirmCallback: ").append(confirmCallbackSet ? "已设置" : "未设置").append("\n");
        sb.append("  连接工厂: ").append(connectionFactoryType).append("\n");

        if (cacheMode != null) {
            sb.append("  缓存模式: ").append(cacheMode).append("\n");
        }

        if (connectionCacheSize != null) {
            sb.append("  连接缓存大小: ").append(connectionCacheSize).append("\n");
        }

        if (channelCacheSize != null) {
            sb.append("  通道缓存大小: ").append(channelCacheSize).append("\n");
        }

        sb.append("  总发送数: ").append(totalSendCount).append("\n");
        sb.append("  总返回数: ").append(totalReturnCount).append("\n");
        sb.append("  总确认数: ").append(totalConfirmCount).append("\n");
        sb.append("  连接状态: ").append(connectionActive ? "活跃" : "断开").append("\n");

        if (error != null) {
            sb.append("  错误: ").append(error).append("\n");
        }

        sb.append("  检查时间: ").append(checkTime);

        return sb.toString();
    }
}
