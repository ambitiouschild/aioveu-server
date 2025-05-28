package com.aioveu.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/29 0029 21:41
 */
@Data
public class BaseItemVO extends IdNameVO {

    private Integer status;

    private Date createDate;

}
