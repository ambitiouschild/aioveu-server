package com.aioveu.data.sync.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description 第三方平台通用场馆对象
 * @author: 雒世松
 * @date: 2025/3/27 16:07
 */
@Data
public class CommonVenue implements Serializable {

    /**
     * 趣数的名称
     */
    private String name;

    private Long id;

    /**
     * 第三方平台名称
     */
    private String aliasName;

    /**
     * 第三方平台id
     */
    private String aliasId;

    /**
     * 场地信息
     */
    private List<CommonField> fields;
}
