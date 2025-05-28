package com.aioveu.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/24 19:29
 */
@Data
public class CouponVerifyItemVO {

    private Long id;

    private Date createDate;

    private String username;

    private String storeName;

    private String templateName;

    /**
     * 核销人
     */
    private String verifyUser;

    private String remark;

    private String gradeName;

    private Date gradeStartTime;

    private Date gradeEndTime;
}
