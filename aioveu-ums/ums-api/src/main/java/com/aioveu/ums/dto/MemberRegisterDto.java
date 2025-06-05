package com.aioveu.ums.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * @Description: TODO 会员传输层对象
 * @Author: 雒世松
 * @Date: 2025/6/5 18:53
 * @param
 * @return:
 **/

@Data
public class MemberRegisterDto {

    private Integer gender;

    private String nickName;

    private String mobile;

    private LocalDate birthday;

    private String avatarUrl;

    private String openid;

    private String sessionKey;

    private String city;

    private String country;

    private String language;

    private String province;

}
