package com.aioveu.system.event;

import lombok.Data;

/**
 * @ClassName: DictEvent
 * @Description TODO  字典更新事件
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 20:08
 * @Version 1.0
 **/

@Data
public class DictEvent {

    /**
     * 字典编码
     */
    private String dictCode;

    /**
     * 时间戳
     */
    private long timestamp;

    public DictEvent(String dictCode) {
        this.dictCode = dictCode;
        this.timestamp = System.currentTimeMillis();
    }
}
