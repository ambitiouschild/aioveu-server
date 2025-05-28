package com.aioveu.data.sync.dp.resp;

import lombok.Data;

/**
 * @description 美团点评 订单请求返回对象
 * @author: 雒世松
 * @date: 2025/3/31 10:47
 */
@Data
public class DpAccount {

    private String accountName;

    private Integer accountType;

    private String customerName;

    private String shopAccount;


}
