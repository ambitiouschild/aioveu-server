package com.aioveu.data.sync.dp.resp;

import lombok.Data;

/**
 * @description 美团点评 账号信息
 * @author: 雒世松
 * @date: 2025/3/31 10:47
 */
@Data
public class DpAccountResponse {

    private DpAccount data;

    private String error;


}
