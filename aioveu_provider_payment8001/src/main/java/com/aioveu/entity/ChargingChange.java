package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description 店铺计费变动明细
 * @author: 雒世松
 * @date: Created in 2025/4/23 11:41
 */
@TableName("sport_charging_change")
@Data
public class ChargingChange extends IdNameEntity {

    private String chargingCode;

    private Long companyId;

    private Long storeId;

    private String description;

    /**
     * 0 支出 1 增加
     */
    private Integer changeType;

    /**
     * 消费次数
     */
    private Integer count;

    /**
     * 剩余次数
     */
    private Integer remainCount;

    /**
     * 产品id
     */
    private String productId;

}
