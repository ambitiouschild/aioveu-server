package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_business_user_info")
@Data
public class BusinessUserInfo extends IdEntity {

    private Long storeId;

    private String phone;

    private String orderDetailId;

    private Long userInfoPublicId;

    private Long exerciseId;

    private String source;

    private String pushUserId;

    private Boolean newUser;

    private String address;

    // status 1 待激活 2 激活 已领取 8 电话失效待审核 10 审核通过(已退款)


}
