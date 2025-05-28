package com.aioveu.vo;

import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/19 17:35
 */
@Data
public class WxMaPayVO {

    private String orderId;

    private String timeStamp;

    private String nonceStr;

    private String packageStr;

    private String signType;

    private String paySign;

    private String prepayId;


}
