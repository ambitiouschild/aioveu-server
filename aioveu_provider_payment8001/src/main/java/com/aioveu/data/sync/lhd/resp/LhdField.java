package com.aioveu.data.sync.lhd.resp;

import lombok.Data;

/**
 * @description 来沪动 场地对象
 * @author: 雒世松
 * @date: 2025/3/31 10:47
 */
@Data
public class LhdField {

    /**
     * 场地id
     */
    private String id;

    /**
     * 场地名称
     */
    private String fieldName;

}