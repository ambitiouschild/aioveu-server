package com.aioveu.dto;

import lombok.Data;

/**
 * @author 用户可用优惠券
 * @description
 * @date 2025/1/22 17:06
 */
@Data
public class UserAvailableCouponDTO {

    private String userId;

    private String phone;

    private Long companyId;

    private Long storeId;

    private String storeName;

    private Integer status;

}
