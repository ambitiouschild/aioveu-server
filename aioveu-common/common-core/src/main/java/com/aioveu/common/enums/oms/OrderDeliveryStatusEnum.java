package com.aioveu.common.enums.oms;

import com.aioveu.common.base.IBaseEnum;
import lombok.Getter;

/**
 * @Description: TODO 物流状态枚举  物流状态【0->已同步微信（已上传发货信息）；1->未同步微信（可发货）】
 * @Author: 雒世松
 * @Date: 2025/6/9 11:51
 * @param
 * @return:
 **/


public enum OrderDeliveryStatusEnum {


    /**
     * 已同步微信（已上传发货信息）
     * 微信侧：发货信息已录入
     */
    SYNCED(0, "已同步微信（已上传发货信息）"),
    /**
     * 未同步微信（可发货）
     * 微信侧：尚未上传发货信息
     */
    PENDING(1, "未同步微信（可发货）"),

    /**
     * 异常 / 未知状态
     * 用于兜底、容错、初始化
     */
    UNKNOWN(3, "未知状态");

    OrderDeliveryStatusEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }


    @Getter
    private Integer value;

    @Getter
    private String label;
}
