package com.aioveu.data.sync.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @description 第三方平台通用场地对象
 * @author: 雒世松
 * @date: 2025/3/27 0014 21:06
 */
@Data
public class CommonField implements Serializable {

    private Long id;

    /**
     * 趣数的名称
     */
    private String name;

    /**
     * 第三方平台名称
     */
    private String aliasName;

    /**
     * 第三方平台id
     */
    private String aliasId;

}