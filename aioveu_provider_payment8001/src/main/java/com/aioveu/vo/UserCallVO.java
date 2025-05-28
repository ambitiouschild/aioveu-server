package com.aioveu.vo;

import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/2/28 0028 21:39
 */
@Data
public class UserCallVO extends UserInfoVO {

    private Long userCallId;

    private Long userReceiveCallId;

    private Integer callStatus;

    private String userId;

    private String oPhone;

    private Long otherUserReceiveCallId;

    private Long unUsedCouponNumber;

}
