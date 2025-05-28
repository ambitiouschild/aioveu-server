package com.aioveu.data.sync.lhd.resp;

import lombok.Data;

import java.util.Date;

/**
 * @description 来沪动 订单请求返回对象
 * @author: 雒世松
 * @date: 2025/3/31 10:47
 */
@Data
public class LhdResponse<T> {

    private String success;

    private T data;

    private String message;

    private Date timestamp;

    public Boolean responseSuccess() {
        return this.getSuccess().equals("T");
    }
}