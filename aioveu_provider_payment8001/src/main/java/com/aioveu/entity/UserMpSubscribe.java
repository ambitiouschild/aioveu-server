package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description 用户公众号关注和取消关注事件
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_user_mp_subscribe")
@Data
public class UserMpSubscribe extends IdEntity{

    private String openId;

    private String unionId;

    private String toUser;

    private String subscribeScene;

    private String qrScene;

    private String qrSceneStr;

    private String appId;

    private String event;

    private String msgType;

    private String eventKey;

    private String ticket;

}
