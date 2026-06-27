package com.aioveu.common.enums.oms;

import com.aioveu.common.base.IBaseEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
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
     * 未同步微信（可发货）
     */
    PENDING(0, "未同步微信（可发货）"),
    /**
     * 已同步微信（已上传发货信息）
     */
    SYNCED(1, "已同步微信（已上传发货信息）"),

    /**
     * 异常 / 未知状态
     * 用于兜底、容错、初始化
     */
    UNKNOWN(3, "未知状态");

    OrderDeliveryStatusEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    //100% 稳妥的「生产级最终版」（强烈推荐）
    @EnumValue
    @Getter
    private Integer value;

    @Getter
    private String label;
}
