package com.aioveu.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/12/1 13:33
 */
@Data
public class OrderVO {

    private String id;

    private String name;

    private String address;

    private Date startTime;

    private Date endTime;

    private Integer status;

    private String image;

    private Integer count;

    private String productId;

    private String appId;

}
