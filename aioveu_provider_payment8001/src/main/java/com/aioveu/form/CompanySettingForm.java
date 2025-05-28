package com.aioveu.form;

import com.aioveu.entity.Company;
import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/3/10 10:16
 */
@Data
public class CompanySettingForm extends Company {

    private Integer beforeBookingMinutes;

    private Integer cancelGradeMinutes;

    /**
     * 单次订场数
     */
    private Integer fieldBookNums;

    private String invoiceContents;

    /**
     * 客户约课教练归属限制，默认0不限制，1限制
     * 不限制：可以约所有教练的课
     * 限制：只可以约自己归属教练的课
     */
    private Integer appointmentCoachLimit;

    private Integer cancelBookingDays;

}
