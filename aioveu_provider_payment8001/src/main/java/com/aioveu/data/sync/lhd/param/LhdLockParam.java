package com.aioveu.data.sync.lhd.param;

import lombok.Data;

import java.util.List;


/**
 * @description 锁场参数类
 * @author: 雒世松
 * @date: 2025/3/31 10:47
 */
@Data
public class LhdLockParam {

    /**
     * 场馆id
     */
    private String stadiumItemId;

    /**
     * 场地信息
     */
    private List<LhdLockItemParam> lockFieldRecords;

}