package com.aioveu.data.sync.dp.resp;

import lombok.Data;

import java.io.Serializable;

/**
 * @description 美团点评场地对象
 * @author: 雒世松
 * @date: 2025/3/27 0014 21:06
 */
@Data
public class DpField implements Serializable {

    private Long productId;

    private String productName;

}