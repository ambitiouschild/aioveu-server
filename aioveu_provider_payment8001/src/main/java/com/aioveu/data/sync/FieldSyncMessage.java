package com.aioveu.data.sync;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description 同步的MQ消息
 * @author: 雒世松
 * @date: 2025/4/18 15:15
 */
@Data
public class FieldSyncMessage implements Serializable {

    /**
     * 场地信息 支持多个场地状态同步
     */
    private List<Long> fieldPlanIdList;

    /**
     * 渠道
     */
    private String channel;

    /**
     * 订场状态 锁场 还是 解锁
     */
    private Integer status;

}
