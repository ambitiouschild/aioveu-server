package com.aioveu.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/2/28 0028 21:39
 */
@Data
public class StoreUserPublicInfoVO {

    private Long id;

    private String phone;

    private String source;

    private Boolean newUser;

    private String exerciseName;

    private String storeName;

    private String address;

    private Date createDate;

    private Integer status;

}
