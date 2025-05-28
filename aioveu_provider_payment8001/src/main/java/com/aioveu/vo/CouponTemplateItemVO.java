package com.aioveu.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/12/21 0021 21:41
 */
@Data
public class CouponTemplateItemVO {

    private Long id;

    private String name;

    private Date createDate;

    private Integer status;

}
