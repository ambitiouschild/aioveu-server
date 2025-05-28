package com.aioveu.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/2/28 0028 21:39
 */
@Data
public class UserInfoVO extends BaseNameVO {

    private String phone;

    private String provinceName;

    private String cityName;

    private String regionName;

    private String businessAreaName;

    private Date lastCall;

    private Integer childAge;


}
