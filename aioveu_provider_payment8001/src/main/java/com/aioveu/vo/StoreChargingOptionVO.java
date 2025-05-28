package com.aioveu.vo;

import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/4/23 18:03
 */
@Data
public class StoreChargingOptionVO extends IdNameCodeVO {

    /**
     * 计费次数
     */
    private Integer total;

}
