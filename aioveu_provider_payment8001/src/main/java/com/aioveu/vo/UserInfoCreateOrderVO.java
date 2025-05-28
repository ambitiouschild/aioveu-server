package com.aioveu.vo;

import lombok.Data;

/**
 * @description 客户资料订单信息
 * @author: 雒世松
 * @date: 2025/2/1 0001 22:14
 */
@Data
public class UserInfoCreateOrderVO {

    private String childName;

    private Integer childGender;

    private Integer childAge;

    /**
     * 家长姓名
     */
    private String name;
}
