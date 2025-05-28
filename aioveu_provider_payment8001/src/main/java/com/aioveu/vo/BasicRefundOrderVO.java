package com.aioveu.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description 基础订单信息
 * @author: 雒世松
 * @date: 2025/03/26 0014 22:29
 */
@Data
public class BasicRefundOrderVO extends BasicOrderVO {

    private Date refundDate;


}
