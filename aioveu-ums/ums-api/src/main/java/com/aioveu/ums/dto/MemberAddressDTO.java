package com.aioveu.ums.dto;

import lombok.Data;

/**
 * @Description: TODO 会员地址传输层对象
 * @Author: 雒世松
 * @Date: 2025/6/5 18:53
 * @param
 * @return:
 **/

@Data
public class MemberAddressDTO {

    private Long id;

    private Long memberId;

    private String consigneeName;

    private String consigneeMobile;

    private String province;

    private String city;

    private String area;

    private String detailAddress;

    private Integer defaulted;

}



