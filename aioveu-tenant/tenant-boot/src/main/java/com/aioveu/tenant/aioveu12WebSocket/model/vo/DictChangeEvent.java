package com.aioveu.tenant.aioveu12WebSocket.model.vo;

import lombok.Data;

/**
 * @ClassName: DictChangeEvent
 * @Description TODO
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/23 12:54
 * @Version 1.0
 **/
@Data
public class DictChangeEvent {

    private String dictCode;
    private long timestamp;

    public DictChangeEvent(String dictCode) {
        this.dictCode = dictCode;
        this.timestamp = System.currentTimeMillis();
    }

}
