package com.aioveu.dto;

import lombok.Data;

/**
 * @author 用户约课提醒
 * @description
 * @date 2025/1/22 17:06
 */
@Data
public class UserGradeTipDTO {

    private Long companyId;

    private Long storeId;

    private String storeName;

    private String userId;

    private String username;

    private String gradeTime;

    /**
     * 未使用课券
     */
    private Integer couponNum;

    /**
     * 已使用课券
     */
    private Integer usedCouponNum;

    /**
     * 已约课待使用课券
     */
    private Integer toUsedCouponNum;

    private String phone;

}
