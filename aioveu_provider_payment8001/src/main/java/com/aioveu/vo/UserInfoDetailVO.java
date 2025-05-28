package com.aioveu.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/2/28 0028 21:39
 */
@Data
public class UserInfoDetailVO extends BaseNameVO {

    private String phone;

    /**
     * 回收时间
     */
    private Date recoveryDate;

    /**
     * 预约时间
     */
    private Date appointmentDate;

    /**
     * 标签
     */
    private List<String> tags;

    /**
     * 客户基本信息
     */
    private UserBaseInfoVO userBaseInfo;



}
