package com.aioveu.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 新客资
 */
@Data
public class NewUserInfoVO {
    private Long id;
    private String name;
    private String userId;

    private Long userInfoId;

    private String phone;

    private String oPhone;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date lastCall;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createDate;

    private String receiveUserName;

    private String orderCategorys;

    private Integer orderInfoStatus;

    private String hasExperience;

}
