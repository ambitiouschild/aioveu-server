package com.aioveu.data.sync.dp.resp;

import lombok.Data;

/**
 * @description 锁场 解锁返回对象
 * @author: 雒世松
 * @date: 2025/3/31 10:48
 */
@Data
public class DpFieldActionResponse {

    private String code;

    private String msg;

    private Boolean data;

    private Boolean success;

}
