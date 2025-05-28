package com.aioveu.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserExtendVO {

    private Long companyId;

    private String brandName;

    private String brandLogo;

    private Integer cancelBookingDays;

    private Integer beforeBookingMinutes;

    private String vipAgreementTemplate;

    private String miniAppId;

    private String invoiceContents;

    private Integer cancelGradeMinutes;;
    /**
     * 单次订场数
     */
    private Integer fieldBookNums;

    /**
     * 客户约课教练归属限制，默认0不限制，1限制
     * 不限制：可以约所有教练的课
     * 限制：只可以约自己归属教练的课
     */
    private Integer appointmentCoachLimit;
}
