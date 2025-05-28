package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_message")
@Data
public class Message extends IdNameEntity {

    private Long companyId;

    private Long storeId;

    @NotEmpty(message = "消息不能为空")
    private String content;

    /**
     * 1 系统消息 2 经营消息
     */
    private Integer msgType;

    /**
     * 本条消息与该用户id有关，记录userId
     */
    private String userId;

    // status 1 未读 2 已读

}
