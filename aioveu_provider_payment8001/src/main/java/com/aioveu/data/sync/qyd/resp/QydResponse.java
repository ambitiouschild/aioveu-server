package com.aioveu.data.sync.qyd.resp;

import lombok.Data;

import java.io.Serializable;

/**
 * @description 趣运动请求返回对象
 * @author: 雒世松
 * @date: 2025/3/27 0014 21:06
 */
@Data
public class QydResponse<T> implements Serializable {

    private int status;

    private T data;

    private String msg;

    private Long time;
}