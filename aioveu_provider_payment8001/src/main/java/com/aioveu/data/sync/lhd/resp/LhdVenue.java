package com.aioveu.data.sync.lhd.resp;

import lombok.Data;

import java.util.List;

/**
 * @description 来沪动 场馆对象
 * @author: 雒世松
 * @date: 2025/3/31 10:47
 */
@Data
public class LhdVenue {

    /**
     * 场馆id
     */
    private String id;

    private boolean disabled;

    /**
     * 场馆名称
     */
    private String stadiumItemName;

    /**
     * 门店id
     */
    private String stadiumId;

    /**
     * 门店名称
     */
    private String stadiumName;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 场地信息
     */
    private List<LhdField> lhdFields;

}