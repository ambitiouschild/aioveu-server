package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description 店铺计费选项
 * @author: 雒世松
 * @date: Created in 2025/4/23 11:41
 */
@TableName("sport_store_charging_option")
@Data
public class StoreChargingOption extends IdNameEntity{

    private String chargingCode;

    private Long companyId;

    private Long storeId;

    /**
     * 计费次数
     */
    private Integer total;

}
