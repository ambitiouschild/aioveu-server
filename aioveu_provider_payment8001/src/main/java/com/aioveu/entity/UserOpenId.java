package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_user_open_id")
@Data
public class UserOpenId extends IdEntity {

    private String userId;

    private String appId;

    private String openId;

    private String unionId;

}
