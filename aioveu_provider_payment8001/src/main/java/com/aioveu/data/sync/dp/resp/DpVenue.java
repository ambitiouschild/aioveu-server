package com.aioveu.data.sync.dp.resp;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description 美团点评场馆内部对象
 * @author: 雒世松
 * @date: 2025/3/27 16:07
 */
@Data
public class DpVenue implements Serializable {

    private String shopName;

    private String shopId;

    private String mtShopId;

    /**
     * 场地信息
     */
    private List<DpField> fields;
}
