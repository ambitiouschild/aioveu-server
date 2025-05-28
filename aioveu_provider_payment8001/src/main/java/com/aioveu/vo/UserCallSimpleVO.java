package com.aioveu.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/2/28 0028 21:39
 */
@Data
public class UserCallSimpleVO  {

    private Long id;

    private String userId;

    private Integer status;

    private Integer intention;

    private String intentionName;

    private Date createDate;

    private String introduce;

    private String name;

}
