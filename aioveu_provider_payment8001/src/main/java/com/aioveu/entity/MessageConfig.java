package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName(value = "sport_message_config", autoResultMap = true)
@Data
public class MessageConfig extends IdEntity {

    private Long companyId;

    private Long storeId;

    @NotEmpty(message = "通知编号不能为空")
    private String noticeCode;

    @NotEmpty(message = "消息编号不能为空")
    private String msgCode;

    /**
     * 消息配置
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> config;

    /**
     * 消息接收人
     */
//    private String receiveUser;

    /**
     * 消息关键key 如果配置了消息没有改字段则不发送消息
     */
    private String msgKey;

    /**
     * 接受人说明信息
     */
    private String receiveUserDesc;
    /**
     *
     */
    private Integer canAddReceiver;
    /**
     * 消息例子
     */
    private String msgExample;


}
