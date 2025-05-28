package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description 用户私海池
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName(value = "sport_user_receive_call")
@Data
public class UserReceiveCall extends IdEntity {

    private Long userInfoId;

    private String userId;

    /**
     * 1教练归属
     * 2销售归属
     */
    private Integer type;

}
